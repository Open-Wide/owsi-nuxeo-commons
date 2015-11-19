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

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.propertysync.PropertySyncException;
import fr.openwide.nuxeo.propertysync.service.PropertySyncService;
import fr.openwide.nuxeo.propertysync.service.RuleDescriptor;
import fr.openwide.nuxeo.propertysync.service.PropertyDescriptor;
import fr.openwide.nuxeo.types.TypeRoot;
import fr.openwide.nuxeo.utils.document.DocumentUtils;

/**
 * @author mkalam-alami
 * @author schambon
 *
 */
public class PropertySyncFromParentsRunner extends AbstractPropertySyncRunner {

    protected final boolean save;

    protected final List<RuleDescriptor> descriptors;
    
    protected PropertySyncFromParentsRunner(CoreSession session, DocumentModel doc,
            List<RuleDescriptor> descriptors, boolean save) {
        super(session, doc);
        this.descriptors = descriptors;
        this.save = save;
    }

    @Override
    public void run() throws NuxeoException {
        
        for (RuleDescriptor d : descriptors) {
            try {
                List<String> overridenProperties = new ArrayList<String>();
                for (PropertyDescriptor propertyDescriptor : d.getPropertyDescriptors()) {
                    if (!overridenProperties.contains(propertyDescriptor.getXpath())) {
                        DocumentModel ancestor = session.getDocument(doc.getParentRef());
                        boolean found = false;
                        while (ancestor != null && ! TypeRoot.TYPE.equals(ancestor.getType())) {
                            if (DocumentUtils.isAssignable(ancestor.getType(), propertyDescriptor.getAncestorType())) {
                                found = true;
                                break;
                            }
                            if (ancestor.getPathAsString().equals("/")) break;
                            ancestor = session.getDocument(ancestor.getParentRef());
                        }
    
                        if (found) {
                            boolean valueCopied = this.copyPropertyValue(ancestor, propertyDescriptor.getAncestorXpath(),
                                    doc, propertyDescriptor.getXpath(), propertyDescriptor.getOnlyIfNull());
                            if (valueCopied) {
                                overridenProperties.add(propertyDescriptor.getXpath());
                            }
                        }
                    }
                }
            }
            catch (NuxeoException e) {
                throw new PropertySyncException("Error while applying descriptor '" + d.name + "'", e);
            }
        }
        
        if (save) {
            doc.putContextData(PropertySyncService.CONTEXT_TRIGGERED_BY_PROPERTY_SYNC, true);
            session.saveDocument(doc);
        }
    }

}
