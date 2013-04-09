package fr.openwide.nuxeo.ordering.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("category")
public class OrderingCategoryDescriptor {

    @XNode("@name")
    protected String name;

    @XNode("@default")
    protected boolean isDefault = false;
    
    @XNodeList(value = "type", type = String[].class, componentType = String.class)
    protected String[] types;

    public String getName() {
        return name;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public String[] getTypes() {
        return types;
    }
    
}
