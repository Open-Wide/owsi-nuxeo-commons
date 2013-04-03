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
package fr.openwide.nuxeo;

import org.nuxeo.ecm.core.api.PathRef;

public interface DefaultHierarchy {
    
    static final String DEFAULT_DOMAIN_PATH_AS_STRING = "/default-domain";

    static final PathRef DEFAULT_DOMAIN_PATH = new PathRef(DEFAULT_DOMAIN_PATH_AS_STRING);
    
    static final String WORKSPACES_PATH_AS_STRING = DEFAULT_DOMAIN_PATH_AS_STRING + "/workspaces";

    static final PathRef WORKSPACES_PATH = new PathRef(WORKSPACES_PATH_AS_STRING);

}
