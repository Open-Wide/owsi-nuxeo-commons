package fr.openwide.nuxeo;

import org.nuxeo.ecm.core.api.PathRef;

public interface DefaultHierarchy {
    
    static final String DEFAULT_DOMAIN_PATH_AS_STRING = "/default-domain";

    static final PathRef DEFAULT_DOMAIN_PATH = new PathRef(DEFAULT_DOMAIN_PATH_AS_STRING);
    
    static final String WORKSPACES_PATH_AS_STRING = DEFAULT_DOMAIN_PATH_AS_STRING + "/workspaces";

    static final PathRef WORKSPACES_PATH = new PathRef(WORKSPACES_PATH_AS_STRING);

}
