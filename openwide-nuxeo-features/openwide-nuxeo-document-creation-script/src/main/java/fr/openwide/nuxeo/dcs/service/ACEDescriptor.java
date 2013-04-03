package fr.openwide.nuxeo.dcs.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject(value = "ace")
public class ACEDescriptor {
    
    @XNode("@granted")
    private boolean granted = true;

    @XNode("@principal")
    private String principal;

    @XNode("@permission")
    private String permission;

    public ACEDescriptor() {
        
    }
    
    public ACEDescriptor(String principal, String permission) {
        this(principal, permission, true);
    }
    
    public ACEDescriptor(String principal, String permission, boolean granted) {
        this.principal = principal;
        this.permission = permission;
        this.granted = granted;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public String getPrincipal() {
        return principal;
    }
    
    public boolean isGranted() {
        return granted;
    }
    
}
