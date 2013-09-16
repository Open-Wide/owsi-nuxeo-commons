
package fr.openwide.nuxeo.automation.wall.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.openwide.nuxeo.automation.wall.automation.ConfigurableWallStreamHandler;

/**
 * @author mkalam-alami
 *
 */
@XObject("handler")
public class ConfigurableWallStreamHandlerDescriptor {

    @XNode("@class")
    protected Class<? extends ConfigurableWallStreamHandler> handlerClass;
    
    @XNode("@enabled")
    protected boolean enabled;
    
    /**
     * @return the className
     */
    public Class<? extends ConfigurableWallStreamHandler> getHandlerClass() {
        return handlerClass;
    }
    
    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
}
