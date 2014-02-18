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

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;

/**
 * 
 * @author mkalam-alami
 *
 */
public interface TypeDocument {

    static final String TYPE = "Document";

    static final String XPATH_UUID = "ecm:uuid";
    
    static final String XPATH_NAME = "ecm:name";
    
    static final String XPATH_TITLE = "dc:title";

    static final String XPATH_DESCRIPTION = "dc:description";

    static final String XPATH_SUBJECTS = "dc:subjects";
    
    static final String XPATH_RIGHTS = "dc:rights";

    static final String XPATH_SOURCE = "dc:source";

    static final String XPATH_COVERAGE = "dc:source";

    static final String XPATH_CREATED = "dc:created";
    
    static final String XPATH_MODIFIED = "dc:modified";

    static final String XPATH_LANGUAGE = "dc:language";

    static final String XPATH_ISSUED = "dc:issued";

    static final String XPATH_VALID = "dc:valid";
    
    static final String XPATH_EXPIRED = "dc:expired";
    
    static final String XPATH_FORMAT = "dc:format";
    
    static final String XPATH_CREATOR = "dc:creator";

    static final String XPATH_CONTRIBUTORS = "dc:contributors";

    static final String XPATH_LAST_CONTRIBUTOR = "dc:lastContributor";

    static final String XPATH_NATURE = "dc:nature";
    
    /**
     * Holds a path (e.g. "/img/file.png")
     */
    static final String XPATH_ICON = "icon";

   
   // adapter setter/getters :

   String getType();
   
   String getUuid();

   String getName();

   String getTitle() throws ClientException;

   void setTitle(String title) throws PropertyException, ClientException;

   String getDescription() throws ClientException;

   void setDescription(String description) throws PropertyException, ClientException;

   Object getProperty(String xpath) throws Exception;

   void setProperty(String xpath, Serializable value) throws Exception;
    
}
