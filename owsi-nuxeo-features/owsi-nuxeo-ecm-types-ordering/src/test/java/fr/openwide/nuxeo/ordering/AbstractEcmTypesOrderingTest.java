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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.actions.seam.SeamActionContext;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.runtime.test.runner.Deploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService;
import fr.openwide.nuxeo.test.AbstractNuxeoTest;

/**
 * 
 * @author mkalam-alami
 * 
 */
@Deploy({
    "org.nuxeo.ecm.platform.types.api",
    "org.nuxeo.ecm.platform.types.core", // Contains the TypeManager service
    "org.nuxeo.ecm.webapp.base", // Default TypeManager contributions
    "fr.openwide.nuxeo.commons.constants",
    "fr.openwide.nuxeo.commons.ecmtypesordering"
})
public abstract class AbstractEcmTypesOrderingTest extends AbstractNuxeoTest {

    private static final Logger logger = Logger.getLogger(AbstractEcmTypesOrderingTest.class);
    
    @Inject
    protected EcmTypesOrderingService ecmTypesOrderingService;

    @Inject
    protected TypeManager typeManager;
    
    @BeforeClass
    public static void init() {
        setLogRepositoryAfterEachTest(false);
    }
    
    protected ActionContext createActionContext(DocumentRef docRef) throws ClientException {
        ActionContext actionContext = new SeamActionContext();
        actionContext.setDocumentManager(documentManager);
        actionContext.setCurrentPrincipal((NuxeoPrincipal) documentManager.getPrincipal());
        actionContext.setCurrentDocument(documentManager.getDocument(docRef));
        return actionContext;
    }

    protected Map<String, List<Type>> getTypesMap(String doctype) {
        return typeManager.getTypeMapForDocumentType(doctype, null);
    }

    protected Map<String, List<Type>> getTypesMap(DocumentRef docRef) throws ClientException {
        DocumentModel documentModel = documentManager.getDocument(docRef);
        return typeManager.getTypeMapForDocumentType(documentModel.getType(), documentModel);
    }
    
    protected Map<String, List<Type>> getTypesMap(String... categoryAndTypes) {
        HashMap<String, List<Type>> typesMap = new HashMap<String, List<Type>>();
        for (String categoryAndType : categoryAndTypes) {
            String[] categoryAndTypeArray = categoryAndType.split("\\:");
            
            String category = null, name = null;
            if (categoryAndTypeArray.length == 2) {
                category = categoryAndTypeArray[0];
                name = categoryAndTypeArray[1];
            }
            else if (categoryAndTypeArray.length == 1) {
                category = "SimpleDocument";
                name = categoryAndTypeArray[0];
            }
            
            if (category != null && name != null) {
                if (!typesMap.containsKey(category)) {
                    typesMap.put(category, new ArrayList<Type>());
                }
                typesMap.get(category).add(typeManager.getType(name));
            }
        }
        return typesMap;
    }

    protected void traceTypes(Map<String, List<Type>> orderTypes) {
        logger.info("-------------------");
        for (Entry<String, List<Type>> category : orderTypes.entrySet()) {
            logger.info("[" + category.getKey() + "]");
            if (!category.getValue().isEmpty()) {
                for (Type type : category.getValue()) {
                    logger.info("* " + type.getLabel());
                }
            }
            else {
                logger.info("  (Nothing)");
            }
        }
        logger.info("-------------------");
    }

}