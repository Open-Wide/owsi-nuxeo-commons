package fr.openwide.nuxeo.utils.jsf;

import java.util.regex.Pattern;

/**
 * @author mkalam-alami
 *
 */
public interface VersionDisplayService {
    
    String getVersionPrefix();
    
    void setVersionPrefix(String versionPrefix);
    
    Pattern getBundleMatchPattern();
    
    void setBundleMatchPattern(Pattern bundleMatchPattern);
    
}
