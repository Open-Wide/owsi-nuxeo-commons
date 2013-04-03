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
@XObject(value = "acl")
public class ACLDescriptor {
    
    @XNode("@blockInheritance")
    private boolean blockInheritance = false;

    @XNodeList(value = "ace", type = ArrayList.class, componentType = ACEDescriptor.class)
    private List<ACEDescriptor> aces = new ArrayList<ACEDescriptor>();

    public ACLDescriptor() {
        
    }
    
    public ACLDescriptor(boolean blockInheritance) {
        this.blockInheritance = blockInheritance;
    }
    
    public boolean isBlockInheritance() {
        return blockInheritance;
    }
    
    public ACLDescriptor setBlockInheritance(boolean blockInheritance) {
        this.blockInheritance = blockInheritance;
        return this;
    }
    
    public ACLDescriptor addACE(String principal, String permission, boolean grant) {
        this.aces.add(new ACEDescriptor(principal, permission, grant));
        return this;
    }
    
    public List<ACEDescriptor> getACEs() {
        return aces;
    }
    
    
    
}
