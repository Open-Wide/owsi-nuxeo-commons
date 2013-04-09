package fr.openwide.nuxeo.ordering.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.actions.ActionFilter;
import org.nuxeo.ecm.platform.actions.DefaultActionFilter;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.openwide.nuxeo.ordering.EcmTypeSortMethod;

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
