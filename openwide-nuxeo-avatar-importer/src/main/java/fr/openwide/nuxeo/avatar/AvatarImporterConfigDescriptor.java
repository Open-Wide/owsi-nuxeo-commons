package fr.openwide.nuxeo.avatar;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("configuration")
public class AvatarImporterConfigDescriptor {

    @XNode("dossierAvatars")
    public String dossierAvatars = null; 
    
}
