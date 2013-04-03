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

public interface TypeFile extends TypeDocument {

    static final String TYPE = "File";

    static final String SCHEMA_FILE = "file";
    
    static final String XPATH_FILENAME = SCHEMA_FILE + ":filename";
    
    static final String XPATH_CONTENT = SCHEMA_FILE + ":content";
    
}
