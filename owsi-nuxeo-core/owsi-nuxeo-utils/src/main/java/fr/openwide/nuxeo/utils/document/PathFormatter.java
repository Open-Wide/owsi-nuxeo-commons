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
package fr.openwide.nuxeo.utils.document;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

import fr.openwide.nuxeo.types.TypeDomain;

/**
 * 
 * Builds a path string suited for display, e.g. : "My domain > My workspace > My file".
 * Inspired by Nuxeo's formatter from {@link AbstractPublishActions}
 * 
 * @author mkalam-alami
 *
 */
public class PathFormatter {
    
    private CoreSession documentManager;
    
    private String separator = " > ";

    private String rootType = TypeDomain.TYPE;
    
    private boolean showLeaf = true;
    
    public PathFormatter(CoreSession documentManager) {
        this.documentManager = documentManager;
    }
    
    /**
     * Defines the string used to join each path element.
     * Defaults to " > ".
     * 
     * @param separator
     * @return
     */
    public PathFormatter setSeparator(String separator) {
        this.separator = separator;
        return this;
    }
    
    /**
     * Allows to select until which document type the parents are browsed to build the path string.
     * Default value is "Domain".
     * 
     * @param rootType
     * @return
     */
    public PathFormatter setRootType(String rootType) {
        if (rootType == null) {
            throw new InvalidParameterException("Root type must not be null");
        }
        this.rootType = rootType;
        return this;
    }
    
    /**
     * Defines whether the document itself must be listed in the path string.
     * Default value is "true".
     * 
     * @param showLeaf
     * @return
     */
    public PathFormatter setShowLeaf(boolean showLeaf) {
        this.showLeaf = showLeaf;
        return this;
    }

    /**
     * Returns the formatted path of a document.
     * 
     * @param documentModel
     * @return
     * @throws NuxeoException
     */
    public String getFormattedPath(DocumentModel documentModel)
            throws NuxeoException {
        List<String> pathFragments = new ArrayList<String>();
        if (showLeaf) {
            getPathFragments(documentModel, pathFragments);
        }
        else {
            getPathFragments(documentManager.getDocument(documentModel.getParentRef()), pathFragments);
        }
        return formatPathFragments(pathFragments);
    }

    protected String formatPathFragments(List<String> pathFragments) {
        StringBuilder fullPath = new StringBuilder(50);
        boolean first = true;
        for (String fragment : pathFragments) {
            if (!first) {
                fullPath.insert(0, this.separator);
            }
            else {
                first = false;
            }
            fullPath.insert(0, fragment);
        }
        return fullPath.toString();
    }

    protected void getPathFragments(DocumentModel documentModel,
            List<String> pathFragments) throws NuxeoException {
        pathFragments.add(documentModel.getTitle());
        if (DocumentUtils.isAssignable(documentModel.getType(), rootType)
                || "/".equals(documentModel.getPathAsString())) {
            return;
        }
        DocumentModel parentDocument;
        if (documentManager.hasPermission(documentModel.getParentRef(), SecurityConstants.READ)) {
            parentDocument = documentManager.getDocument(documentModel.getParentRef());
            getPathFragments(parentDocument, pathFragments);
        }
    }
}
