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

import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

import fr.openwide.nuxeo.propertysync.PropertySyncException;
import fr.openwide.nuxeo.propertysync.service.PropertySyncService;
import fr.openwide.nuxeo.propertysync.service.RuleDescriptor;
import fr.openwide.nuxeo.propertysync.service.PropertyDescriptor;
import fr.openwide.nuxeo.utils.document.DocumentUtils;

/**
 * @author mkalam-alami
 * @author schambon
 *
 */
public class PropertySyncChildrenRunner extends AbstractPropertySyncRunner {

    protected static final String TEMPLATE_QUERY_CHILDREN_BY_DOCTYPE = "SELECT * FROM ? " +
            " WHERE " + NXQL.ECM_PATH + " STARTSWITH '?'" +
            " AND " + NXQL.ECM_ISVERSION + " = 0";
    
    protected static final String TEMPLATE_QUERY_CHILDREN_BY_FACET = "SELECT * FROM Document " +
            " WHERE " + NXQL.ECM_MIXINTYPE + " = '?'" +
            " AND " + NXQL.ECM_PATH + " STARTSWITH '?'" +
            " AND " + NXQL.ECM_ISVERSION + " = 0";
    
    protected PropertySyncService propertySyncService;

    protected PropertySyncChildrenRunner(CoreSession session, DocumentModel doc, PropertySyncService propertySyncService) {
        super(session, doc);
        this.propertySyncService = propertySyncService;
    }

    public void run() throws ClientException {
        String doctype = doc.getType();
        
        Map<String, RuleDescriptor> descriptorsByDoctypes = propertySyncService.getTargetDoctypes(doctype);
        if (descriptorsByDoctypes != null && descriptorsByDoctypes.size() > 0) {
            for (Entry<String, RuleDescriptor> entry : descriptorsByDoctypes.entrySet()) {
                RuleDescriptor descriptor = entry.getValue();
                if (!descriptor.getNoMassUpdate()) {
                    String queryByDoctype = NXQLQueryBuilder.getQuery(TEMPLATE_QUERY_CHILDREN_BY_DOCTYPE,
                            new String[]{ entry.getKey(), doc.getPathAsString() }, false, true);
                    copyToChildren(session, doc, queryByDoctype, descriptor);
                }
            }
        }

        Map<String, RuleDescriptor> descriptorsByFacets = propertySyncService.getTargetFacets(doctype);
        if (descriptorsByFacets != null && descriptorsByFacets.size() > 0) {
            for (Entry<String, RuleDescriptor> entry : descriptorsByFacets.entrySet()) {
                RuleDescriptor descriptor = entry.getValue();
                if (!descriptor.getNoMassUpdate()) {
                    String queryByFacets = NXQLQueryBuilder.getQuery(TEMPLATE_QUERY_CHILDREN_BY_FACET,
                            new String[]{ entry.getKey(), doc.getPathAsString() }, false, true);
                    copyToChildren(session, doc, queryByFacets, descriptor);
                }
            }
        }
    }

    protected void copyToChildren(CoreSession coreSession, DocumentModel doc, String childrenQuery, RuleDescriptor d)
            throws ClientException {
        try {
            DocumentModelList children = coreSession.query(childrenQuery);
            for (PropertyDescriptor propertyDescriptor : d.getPropertyDescriptors()) {
                if (doc.getProperty(propertyDescriptor.getAncestorXpath()).isDirty()) {
                    if (DocumentUtils.isAssignable(doc.getType(), propertyDescriptor.getAncestorType())) {
                        for (DocumentModel child : children) {
                            copyPropertyValue(doc, propertyDescriptor.getAncestorXpath(), child, propertyDescriptor.getXpath(),
                                    propertyDescriptor.getOnlyIfNull());
                            child.putContextData(PropertySyncService.CONTEXT_TRIGGERED_BY_PROPERTY_SYNC, true);
                        }
                    }
                }
            }
            coreSession.saveDocuments(children.toArray(new DocumentModel[] {}));
        } catch (ClientException e) {
            throw new PropertySyncException("Error while applying descriptor '" + d.name + "'", e);
        }
    }
}
