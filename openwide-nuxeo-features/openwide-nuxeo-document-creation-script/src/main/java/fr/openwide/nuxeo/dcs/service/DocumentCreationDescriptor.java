package fr.openwide.nuxeo.dcs.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.openwide.nuxeo.types.TypeFolder;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject("document")
public class DocumentCreationDescriptor {

    @XNode("@path")
    public String path = null;
    
    @XNode("@doctype")
    public String doctype = TypeFolder.TYPE;

    @XNode("@title")
    public String title = null;
    
    @XNode(value = "acl")
    public ACLDescriptor acl = new ACLDescriptor();
    
    @XNodeList(value = "properties/property", type = ArrayList.class, componentType = PropertyDescriptor.class)
    public List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();

    @XNodeList(value = "allowedTypes/type", type = ArrayList.class, componentType = String.class)
    public List<String> allowedTypes = new ArrayList<String>();
    
    @XNodeList(value = "facets/facet", type = ArrayList.class, componentType = String.class)
    public List<String> facets = new ArrayList<String>();

    public DocumentCreationDescriptor() {
    }
    
    public DocumentCreationDescriptor(String path) {
        this.path = path;
    }

    public DocumentCreationDescriptor(String path, String doctype) {
        this.path = path;
        this.doctype = doctype;
    }

    public DocumentCreationDescriptor setBlobkInheritance(boolean blockInheritance) {
        this.acl.setBlockInheritance(blockInheritance);
        return this;
    }
    
    public DocumentCreationDescriptor addACE(String principal, String permission, boolean grant) {
        this.acl.addACE(principal, permission, grant);
        return this;
    }
    
    public DocumentCreationDescriptor addProperty(String xpath, String value) {
        this.properties.add(new PropertyDescriptor(xpath, value));
        return this;
    }
    
    public DocumentCreationDescriptor addFacet(String facet) {
        this.facets.add(facet);
        return this;
    }

    public DocumentCreationDescriptor addAllowedType(String type) {
        this.allowedTypes.add(type);
        return this;
    }
    
    public DocumentCreationDescriptor setDoctype(String doctype) {
        this.doctype = doctype;
        return this;
    }
    
    public DocumentCreationDescriptor setPath(String path) {
        this.path = path;
        return this;
    }
    
}
