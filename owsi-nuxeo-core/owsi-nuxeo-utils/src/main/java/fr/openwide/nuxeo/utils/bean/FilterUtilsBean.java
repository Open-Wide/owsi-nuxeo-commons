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
package fr.openwide.nuxeo.utils.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

/**
 * 
 * @author mkalam-alami
 *
 */
@Name("filterUtils")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION)
public class FilterUtilsBean implements Serializable {

    private static final String CONTENT_VIEW_DISPLAY_SCHEMA = "content_view_display";
    
    private static final long serialVersionUID = 1L;

    @In
    protected transient DocumentModel currentDocument;

    @In(required = false)
    protected transient DocumentModel searchDocument;

    /**
     * Returns an NXQL query expression to allow for a clever filtering in content views:
     * - List direct children by default
     * - Search among all children recursively if a filter is enabled 
     * 
     * 
     * NUXEO STUDIO EXAMPLE
     * 
     * In your content views, set the 'query filter' to
     *  
     *   ecm:mixinType != 'HiddenInNavigation' AND ecm:isCheckedInVersion = 0 
     *   AND ecm:currentLifeCycleState != 'deleted' AND ?
     *  
     * And set a unique 'query parameter' to
     * 
     *   #{filterUtils.getChildrenSelectionQueryPart()}
     *   
     * You'll have to disable the advanced options 'Escape parameters' and 'Quote parameters',
     * so be careful: if you have other parameters, you'll need to escape them with
     * 
     *   #{filterUtils.escape(...)}
     * 
     */
    public String getChildrenSelectionQueryPart() throws NuxeoException {
        if (isSearchDocumentEmpty()) {
            return "ecm:parentId = '" + currentDocument.getId() + "'";
        } else {
            return "ecm:path STARTSWITH '" + escape(currentDocument.getPathAsString()) + "'";
        }
    }

    public String escape(String s) throws NuxeoException {
        return NXQLQueryBuilder.prepareStringLiteral(s, false, true);
    }

    public boolean isSearchDocumentEmpty() throws NuxeoException {
        if (searchDocument != null) {
            for (String schema : searchDocument.getSchemas()) {
                if (!CONTENT_VIEW_DISPLAY_SCHEMA.equals(schema)) {
                    for (Object value : searchDocument.getProperties(schema).values()) {
                        if (value != null) {
                            if (value instanceof String[] && ((String[]) value).length == 0) {
                                continue;
                            }
                            if (value instanceof String && StringUtils.isBlank((String) value)) {
                                continue;
                            }
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
