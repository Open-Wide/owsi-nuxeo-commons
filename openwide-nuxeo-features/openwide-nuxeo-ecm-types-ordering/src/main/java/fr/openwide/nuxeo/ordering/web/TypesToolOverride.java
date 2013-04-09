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
package fr.openwide.nuxeo.ordering.web;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.ecm.webapp.action.ActionContextProvider;
import org.nuxeo.ecm.webapp.action.TypesTool;

import fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService;

/**
 * 
 * @author mkalam-alami
 * 
 */
@Name("typesTool")
@Scope(CONVERSATION)
@Install(precedence = Install.FRAMEWORK + 1) // Override
public class TypesToolOverride extends TypesTool implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final Log log = LogFactory.getLog(TypesToolOverride.class);

    @In(create = true)
    protected DocumentModel currentDocument;

    @In(create = true)
    protected EcmTypesOrderingService ecmTypesOrdering;

    @In(create = true)
    protected ActionContextProvider actionContextProvider;

    @Override
    protected Map<String, List<List<Type>>> organizeType(Map<String, List<Type>> types) {
        Map<String, List<Type>> categorizedTypes = ecmTypesOrdering.orderTypes(types,
                actionContextProvider.createActionContext());
        return splitTypesOnColumns(categorizedTypes, ecmTypesOrdering.getColumnSize());
    }

    private Map<String, List<List<Type>>> splitTypesOnColumns(
            Map<String, List<Type>> categorizedTypes, int columnSize) {
        Map<String, List<List<Type>>> newTypesMap = new LinkedHashMap<String, List<List<Type>>>();
        Set<Entry<String, List<Type>>> typeEntrySet = categorizedTypes.entrySet();
        for (Entry<String, List<Type>> set : typeEntrySet) {
            List<Type> typeList = set.getValue();
            List<List<Type>> newList = new ArrayList<List<Type>>();
            int index = 0;
            newList.add(index, new ArrayList<Type>());
            for (Type type : typeList) {
                List<Type> currentList = newList.get(index);
                if (currentList == null) {
                    currentList = new ArrayList<Type>();
                    newList.add(index, currentList);
                }
                currentList.add(type);
                if (currentList.size() % columnSize == 0) {
                    index++;
                    newList.add(index, new ArrayList<Type>());
                }
            }
            newTypesMap.put(set.getKey(), newList);
        }
        return newTypesMap;
    }
}
