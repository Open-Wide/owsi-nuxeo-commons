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

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.types.TypeDocument;

/**
 * @author mkalam-alami
 *
 */
public class SimpleSelectItemLabelBuilder implements SelectItemLabelBuilder {

    private final String xpath;
    
    public SimpleSelectItemLabelBuilder() {
        xpath = TypeDocument.XPATH_TITLE;
    }
    
    public SimpleSelectItemLabelBuilder(String property) {
        xpath = property;
    }
    
    @Override
    public String getLabel(DocumentModel model) throws NuxeoException {
        return (String) model.getPropertyValue(xpath);
    }
    
}
