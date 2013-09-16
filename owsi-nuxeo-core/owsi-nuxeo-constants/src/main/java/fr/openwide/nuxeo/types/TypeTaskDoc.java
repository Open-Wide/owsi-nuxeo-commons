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
public interface TypeTaskDoc extends TypeDocument {

    static final String TYPE = "TaskDoc";

    /**
     * String list
     */
    static final String XPATH_ACTORS = "nt:actors";

    /**
     * String
     */
    static final String XPATH_INITIATOR = "nt:initiator";

    /**
     * String
     */
    static final String XPATH_TARGET_DOCUMENT_ID = "nt:targetDocumentId";

    /**
     * String
     */
    static final String XPATH_DIRECTIVE = "nt:directive";
    /**
     * String
     */
    static final String XPATH_TASK_NAME = "nt:name";
    
    /**
     * String
     * Since 5.6
     */
    static final String XPATH_TYPE = "nt:type";
    
    /**
     * String
     * Since 5.6
     */
    static final String XPATH_PROCESS_ID = "nt:processId";

    /**
     * String
     */
    static final String XPATH_ACCEPTED = "nt:accepted";
    
    /**
     * String
     */
    static final String XPATH_DUE_DATE = "nt:dueDate";
    
    /**
     * List of "string => object" maps with entries:
     *   "key" = string
     *   "value" = string
     */
    static final String XPATH_TASK_VARIABLES = "nt:task_variables";
    static final String TASK_VARIABLE_ROUTE_INSTANCE_DOC_ID = "routeInstanceDocId";
    
    /**
     * List of "string => object" maps with entries:
     *  "author" = string
     *  "text" = string
     *  "creationDate" = date
     */
    static final String XPATH_TASK_COMMENTS = "nt:taskComments";
}
