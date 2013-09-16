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
package fr.openwide.nuxeo.ordering.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.openwide.nuxeo.ordering.EcmTypeComparator;
import fr.openwide.nuxeo.ordering.EcmTypeSortMethod;

/**
 * 
 * @author mkalam-alami
 *
 */
public class EcmTypesOrderingServiceImpl extends DefaultComponent implements EcmTypesOrderingService {

    public static final String EXTENSION_POINT_ORDERING = "ordering";

    public static final String EXTENSION_POINT_CONFIG = "config";
    
    public static final String DEFAULT_DEFAULT_CATEGORY = "misc";
    
    public static final int DEFAULT_COLUMN_SIZE = 4;

    private static final Logger logger = Logger.getLogger(EcmTypesOrderingServiceImpl.class);
    
    private Map<String, OrderingDescriptor> orderingDescriptors = new HashMap<String, OrderingDescriptor>();
    
    private OrderingConfigDescriptor orderingConfig = new OrderingConfigDescriptor();

    private ActionManager actionManager;
    
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor) throws Exception {
        if (EXTENSION_POINT_ORDERING.equals(extensionPoint)) {
            OrderingDescriptor descriptor = (OrderingDescriptor) contribution;
            orderingDescriptors.put(descriptor.getName(), descriptor); // Warning: Merging not implemented
        }
        else if (EXTENSION_POINT_CONFIG.equals(extensionPoint)) {
            orderingConfig = (OrderingConfigDescriptor) contribution;
        }
    }
    
    public int getColumnSize() {
        return orderingConfig.getColumnSize();
    }

    public Map<String, List<Type>> orderTypes(Map<String, List<Type>> types, ActionContext context) {
        OrderingDescriptor orderingDescriptor = getOrderingDescriptor(context);
        if (orderingDescriptor != null) {
            // Build a map with all available types, by name
            Map<String, Type> allTypes = new HashMap<String, Type>();
            Map<String, String> initialCategories = new HashMap<String, String>();
            for (Entry<String, List<Type>> typesCategory : types.entrySet()) {
                for (Type type : typesCategory.getValue()) {
                    allTypes.put(type.getId(), type);
                    initialCategories.put(type.getId(), typesCategory.getKey());
                }
            }
            
            // Remove hidden types
            for (String hiddenType : orderingDescriptor.getHiddenTypes()) {
                allTypes.remove(hiddenType);
            }
            
            // Sort them by category
            Map<String, List<Type>> categorizedTypes = new LinkedHashMap<String, List<Type>>();
            OrderingCategoryDescriptor[] categories = orderingDescriptor.getCategories();
            for (OrderingCategoryDescriptor category : categories) {
                ArrayList<Type> currentCategoryList = new ArrayList<Type>();
                categorizedTypes.put(category.getName(), currentCategoryList);
                for (String type : category.getTypes()) {
                    Type removedType = allTypes.remove(type);
                    if (removedType != null) {
                        currentCategoryList.add(removedType);
                    }
                }
            }
            
            // Append the rest to their initial category
            for (Type type : allTypes.values()) {
                String category = initialCategories.get(type.getId());
                if (!categorizedTypes.containsKey(category)) {
                    categorizedTypes.put(category, new ArrayList<Type>());
                }
                categorizedTypes.get(category).add(type);
            }
            
            // Sort each column
            EcmTypeSortMethod sortMethod = orderingDescriptor.getSortMethod();
            EcmTypeComparator ecmTypeComparator = new EcmTypeComparator(sortMethod);
            if (sortMethod != EcmTypeSortMethod.MANUAL) {
                for (List<Type> categoryTypes : categorizedTypes.values()) {
                    Collections.sort(categoryTypes, ecmTypeComparator);
                }
            }
            
            return categorizedTypes;
        }
        else {
            // Do nothing
            return types;
        }
    }
    
    protected OrderingDescriptor getOrderingDescriptor(ActionContext context) {
        ActionManager actionManager = getActionManager();
        OrderingDescriptor matchingDescriptor = null;
        for (OrderingDescriptor descriptor : orderingDescriptors.values()) {
            if (descriptor.isEnabled() && descriptor.accept(actionManager, context)) {
                if (matchingDescriptor == null || descriptor.getPriority() < matchingDescriptor.getPriority()) {
                    matchingDescriptor = descriptor;
                }
            }
        }
        return matchingDescriptor;
    }

    protected ActionManager getActionManager() {
        if (actionManager == null) {
            try {
                actionManager = Framework.getService(ActionManager.class);
            } catch (Exception e) {
                logger.error("ActionManager not found");
            }
        }
        return actionManager;
    }
    
}
