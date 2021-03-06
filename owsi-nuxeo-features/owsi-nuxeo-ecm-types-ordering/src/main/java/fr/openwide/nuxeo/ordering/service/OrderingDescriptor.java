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
import java.util.Arrays;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.actions.ActionFilter;
import org.nuxeo.ecm.platform.actions.DefaultActionFilter;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.openwide.nuxeo.ordering.EcmTypeSortMethod;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject("ordering")
public class OrderingDescriptor {
    
    public enum SortMethod {
        alphabetical,
        manual;
        
        public EcmTypeSortMethod toEcmTypeSortMethod() {
            if (this == alphabetical) {
                return EcmTypeSortMethod.ALPHABETICAL;
            }
            else {
                return EcmTypeSortMethod.MANUAL;
            }
        }
    }
    
    @XNode("@name")
    protected String name;
    
    @XNode("@enabled")
    protected boolean enabled = true;

    @XNode("@priority")
    protected int priority = 150; // Lower number is higher priority
    
    @XNode("@sort")
    protected SortMethod sortMethod = SortMethod.alphabetical;

    @XNodeList(value = "filter-id", type = ArrayList.class, componentType = String.class)
    protected List<String> filterIds;

    @XNodeList(value = "filter", type = ActionFilter[].class, componentType = DefaultActionFilter.class)
    protected ActionFilter[] filters;
    
    @XNodeList(value = "category", type = OrderingCategoryDescriptor[].class, componentType = OrderingCategoryDescriptor.class)
    protected OrderingCategoryDescriptor[] categories;

    @XNodeList(value = "hiddenTypes/type", type = String[].class, componentType = String.class)
    protected String[] hiddenTypes;
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public EcmTypeSortMethod getSortMethod() {
        return sortMethod.toEcmTypeSortMethod();
    }
    
    public ActionFilter[] getFilters() {
        return filters;
    }
    
    public List<String> getHiddenTypes() {
        return Arrays.asList(hiddenTypes);
    }
    
    public void addFilterId(String filterId) {
        filterIds.add(filterId);
    }
    
    public boolean accept(ActionManager actionManager, ActionContext context) {
        for (String filterId : filterIds) {
            if (!actionManager.checkFilter(filterId, context)) {
                return false;
            }
        }
        return true;
    }
    
    public OrderingCategoryDescriptor[] getCategories() {
        return categories;
    }
    
}
