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
package fr.openwide.nuxeo.utils.jsf;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * 
 * @author mkalam-alami
 *
 */
@Name("contentViewFilters")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION)
public class ContentViewFiltersBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @In
    protected transient DocumentModel currentDocument;

    @In(required = false)
    protected transient DocumentModel searchDocument;
    
    /**
     * Returns an NXQL clause that can be used on content views
     * to make the filter be able to search in sub-folders.
     * 
     * Important: If used through Studio, disable the "Quote" and "Escape"
     * checkboxes from the content view advanced options.
     */
    public String getDocumentContextNXQLClause() throws ClientException {
        if (isSearchDocumentEmpty()) {
            return " ecm:parentId = '" + currentDocument.getId() + "' ";
        }
        else {
            return " ecm:path STARTSWITH '" + currentDocument.getPathAsString() + "' ";
        }
    }
    
    public boolean isSearchDocumentEmpty() throws ClientException {
        if (searchDocument != null) {
            for (String schema : searchDocument.getSchemas()) {
                for (Object value : searchDocument.getProperties(schema).values()) {
                    if (value != null) {
                        if (value instanceof String) {
                            if (!StringUtils.isBlank((String) value)) {
                                return false;
                            }
                        }
                        else if (value instanceof String[]) {
                            if (((String[]) value).length > 0) {
                                return false;
                            }
                        }
                        else if (value instanceof List) {
                            if (((List<?>) value).size() > 0) {
                                return false;
                            }
                        }
                        else if (value instanceof Boolean) {
                            if (Boolean.valueOf((Boolean) value)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
}
