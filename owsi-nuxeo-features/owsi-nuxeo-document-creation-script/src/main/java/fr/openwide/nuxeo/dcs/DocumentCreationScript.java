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
package fr.openwide.nuxeo.dcs;

import java.security.InvalidParameterException;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.dcs.service.DocumentCreationDescriptor;

/**
 * 
 * @author mkalam-alami
 *
 */
public interface DocumentCreationScript {

    String getName();
    
    void run(CoreSession session, boolean overwrite) throws NuxeoException;
    
    void run(CoreSession session, DocumentModel context, boolean overwrite) throws NuxeoException;

    void appendDocumentCreation(DocumentCreationDescriptor descriptor) throws InvalidParameterException;

}
