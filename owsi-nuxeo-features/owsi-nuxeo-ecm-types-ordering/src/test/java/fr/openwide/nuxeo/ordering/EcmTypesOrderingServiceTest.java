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
package fr.openwide.nuxeo.ordering;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import fr.openwide.nuxeo.DefaultHierarchy;
import fr.openwide.nuxeo.types.TypeFolder;

/**
 * 
 * @author mkalam-alami
 * 
 */
@LocalDeploy("fr.openwide.nuxeo.commons.ecmtypesordering:OSGI-INF/ecm-types-ordering-test.xml")
public class EcmTypesOrderingServiceTest extends AbstractEcmTypesOrderingTest {
    
    @Test
    public void testOrdering() throws NuxeoException {
        DocumentModel folderModel = documentManager.createDocumentModel(
                DefaultHierarchy.WORKSPACES_PATH_AS_STRING, "folder", TypeFolder.TYPE);
        folderModel = documentManager.createDocument(folderModel);
        
        Map<String, List<Type>> orderTypes = ecmTypesOrderingService.orderTypes(
                getTypesMap(folderModel.getRef()),
                createActionContext(folderModel.getRef()));
        traceTypes(orderTypes);
    }

}
