package fr.openwide.nuxeo.dcs.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.dcs.DocumentCreationScript;

/**
 * 
 * Similar to the ContentTemplate service, except:
 * - It allows to set facets and local configuration
 * - It allows to set either absolute or relative paths
 * - The content creation execution is entirely manual
 * 
 * @author mkalam-alami
 *
 */
public interface DocumentCreationScriptService {

    void registerScript(DocumentCreationScript script);

    void setScriptEnabled(String name, boolean enabled);
    
    void runScript(CoreSession session, String name, boolean overwrite) throws ClientException;
    
    void runScript(CoreSession session, String name, DocumentModel context, boolean overwrite) throws ClientException;
}
