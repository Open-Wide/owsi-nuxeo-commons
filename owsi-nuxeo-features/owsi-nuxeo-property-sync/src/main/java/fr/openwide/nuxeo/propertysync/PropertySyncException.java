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
package fr.openwide.nuxeo.propertysync;

import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * 
 * @author mkalam-alami
 *
 */
public class PropertySyncException extends NuxeoException {
    
    private static final long serialVersionUID = 1L;

    public PropertySyncException() {
    }

    public PropertySyncException(String message) {
        super(message);
    }

    public PropertySyncException(String message, NuxeoException cause) {
        super(message, cause);
    }

    public PropertySyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertySyncException(Throwable cause) {
        super(cause);
    }

    public PropertySyncException(NuxeoException cause) {
        super(cause);
    }

}
