package fr.openwide.nuxeo.test.directories;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("directory")
public class SqlDirectoryReferenceDescriptor {

    @XNode("@name")
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}
