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
package fr.openwide.nuxeo.types;

/**
 * 
 * @author mkalam-alami
 *
 */
public interface TypeDocument {

    static final String TYPE = "Document";
    
    static final String XPATH_TITLE = "dc:title";
    
    static final String XPATH_MODIFIED = "dc:modified";

    static final String XPATH_DESCRIPTION = "dc:description";

    static final String XPATH_CREATOR = "dc:creator";
    
    /**
     * Holds a path (e.g. "/img/file.png")
     */
    static final String XPATH_ICON = "icon";
    
}
