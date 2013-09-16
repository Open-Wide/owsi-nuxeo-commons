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
package fr.openwide.nuxeo.dcs.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.dcs.DocumentCreationScript;

/**
 * 
 * Similar to the ContentTemplate service, except:
 * - It allows to set facets and local configuration
 * - It allows to set either absolute or relative paths
 * - The content creation execution is entirely manual
 * 
 * @author mkalam-alami
 *
 */
public interface DocumentCreationScriptService {

    void registerScript(DocumentCreationScript script);

    void setScriptEnabled(String name, boolean enabled);
    
    void runScript(CoreSession session, String name, boolean overwrite) throws ClientException;
    
    void runScript(CoreSession session, String name, DocumentModel context, boolean overwrite) throws ClientException;
}
