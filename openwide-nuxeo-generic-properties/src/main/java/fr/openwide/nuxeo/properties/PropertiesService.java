package fr.openwide.nuxeo.properties;

import java.util.List;
import java.util.Map;

/**
 * Basic properties service, for information that doesn't deserve its own extension point.
 * 
 * @author mkalam-alami
 *
 */
public interface PropertiesService {

    String getValue(String key);

    String getValue(String key, String defaultValue);

    Long getNumberValue(String key);

    Long getNumberValue(String key, Long defaultValue);
    
    List<String> getListValue(String key);

    List<String> getListValue(String key, List<String> defaultValue);

    Map<String, String> getMapValue(String key);
    
    Map<String, String> getMapValue(String key, Map<String, String> defaultValue);
}
