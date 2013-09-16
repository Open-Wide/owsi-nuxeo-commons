
package fr.openwide.nuxeo.automation.wall.service;

import java.util.List;

import fr.openwide.nuxeo.automation.wall.automation.ConfigurableWallStreamHandler;

/**
 * @author mkalam-alami
 *
 */
public interface ConfigurableWallStreamService {
    
    List<ConfigurableWallStreamHandler> getConfigurableWallStreamHandlers();

}
