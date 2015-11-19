/*******************************************************************************
 * (C) Copyright 2013 Open Wide (http://www.openwide.fr/) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 ******************************************************************************/
package fr.openwide.nuxeo.propertysync.listener;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.propertysync.service.PropertySyncService;
import fr.openwide.nuxeo.propertysync.service.RuleDescriptor;

/**
 * @author schambon
 * @author mkalam-alami
 */
public class PropertySyncListener implements EventListener {
    
    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(PropertySyncListener.class);
    
    private PropertySyncService propertySyncService;

    @Override
    public void handleEvent(Event event) throws NuxeoException {
        PropertySyncService propertySync = getPropertySyncService();
        DocumentEventContext ctx = (DocumentEventContext) event.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        CoreSession coreSession = ctx.getCoreSession();
        
        if (doc.getContextData(PropertySyncService.CONTEXT_BYPASS_PROPERTY_SYNC) != null
                || doc.isVersion() || doc.isProxy()) {
            return;
        }
        
        if (DocumentEventTypes.BEFORE_DOC_UPDATE.equals(event.getName())) {
            // Copy to children
            new PropertySyncChildrenRunner(coreSession, doc, propertySync).runUnrestricted();
        }
        else {
            List<RuleDescriptor> descriptors = propertySync.getDescriptors(doc);

            if (descriptors != null && descriptors.size() > 0) {
                if (DocumentEventTypes.EMPTY_DOCUMENTMODEL_CREATED.equals(event.getName())) {
                    String parentPath = (String) ctx.getProperty("parentPath");
                    if (parentPath != null && !parentPath.isEmpty()) {
                        doc.setPathInfo(parentPath, null);
                    }
                    else {
                        return;
                    }
                }
                // Copy from ancestors
                new PropertySyncFromParentsRunner(coreSession, doc, descriptors,
                        !DocumentEventTypes.EMPTY_DOCUMENTMODEL_CREATED.equals(event.getName())).runUnrestricted();
            }
        }
    }

    private PropertySyncService getPropertySyncService() throws NuxeoException {
        try {
            if (propertySyncService == null) {
                propertySyncService = Framework.getService(PropertySyncService.class);
            }
            return propertySyncService;
        } catch (Exception e) {
            throw new NuxeoException("Cannot get service", e);
        }
    }
}
