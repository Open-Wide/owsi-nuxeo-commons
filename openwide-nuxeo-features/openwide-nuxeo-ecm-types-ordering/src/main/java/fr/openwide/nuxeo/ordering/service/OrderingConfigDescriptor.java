package fr.openwide.nuxeo.ordering.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("config")
public class OrderingConfigDescriptor {

    @XNode("columnSize")
    protected int columnSize = EcmTypesOrderingServiceImpl.DEFAULT_COLUMN_SIZE;
    
    public int getColumnSize() {
        return columnSize;
    }
    
}
