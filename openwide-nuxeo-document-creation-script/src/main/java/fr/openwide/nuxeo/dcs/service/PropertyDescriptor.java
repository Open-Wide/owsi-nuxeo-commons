package fr.openwide.nuxeo.dcs.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject(value = "property")
public class PropertyDescriptor {

    @XNode("@xpath")
    private String xpath;

    @XNode("@value")
    private String value;

    public PropertyDescriptor() {
        
    }
    
    public PropertyDescriptor(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }
    
    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return value;
    }

}
