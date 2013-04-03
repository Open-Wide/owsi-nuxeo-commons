package fr.openwide.nuxeo.dcs.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject("script")
public class DocumentCreationScriptDescriptor {

    @XNode("@name")
    public String name;

    @XNode("@enabled")
    public boolean enabled = true;

    @XNode("@append")
    public boolean append = false;
    
    @XNodeList(value = "document", type = ArrayList.class, componentType = DocumentCreationDescriptor.class)
    public List<DocumentCreationDescriptor> creationDescriptors;
    
}
