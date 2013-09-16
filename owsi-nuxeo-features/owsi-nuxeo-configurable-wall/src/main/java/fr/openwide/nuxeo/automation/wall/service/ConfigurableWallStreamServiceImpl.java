package fr.openwide.nuxeo.automation.wall.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.openwide.nuxeo.automation.wall.automation.ConfigurableWallStreamHandler;

/**
 * @author mkalam-alami
 * 
 */
public class ConfigurableWallStreamServiceImpl extends DefaultComponent implements ConfigurableWallStreamService {

    private List<ConfigurableWallStreamHandler> handlers = new ArrayList<ConfigurableWallStreamHandler>();
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        ConfigurableWallStreamHandlerDescriptor descriptor = (ConfigurableWallStreamHandlerDescriptor) contribution;
        
        ConfigurableWallStreamHandler existingHandler = null;
        for (ConfigurableWallStreamHandler handler : handlers) {
            if (handler.getClass().equals(descriptor.getHandlerClass())) {
                existingHandler = handler;
                break;
            }
        }
            
        // Add
        if (descriptor.isEnabled() && existingHandler == null) {
            handlers.add(descriptor.getHandlerClass().newInstance());
        }
        // Remove
        else if (!descriptor.isEnabled() && existingHandler != null) {
            handlers.remove(existingHandler);
        }
    }

    /* (non-Javadoc)
     * @see fr.openwide.nuxeo.automation.wall.service.ConfigurableWallStreamService#getConfigurableWallStreamHandlers()
     */
    public List<ConfigurableWallStreamHandler> getConfigurableWallStreamHandlers() {
        return handlers;
    }
    
}
