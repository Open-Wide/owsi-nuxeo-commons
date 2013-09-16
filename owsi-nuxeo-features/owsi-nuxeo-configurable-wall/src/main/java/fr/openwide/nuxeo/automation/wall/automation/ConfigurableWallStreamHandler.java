
package fr.openwide.nuxeo.automation.wall.automation;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @author mkalam-alami
 *
 */
public interface ConfigurableWallStreamHandler {
    
    boolean accept(CoreSession documentManager, String contextObject) throws ClientException;
    
    boolean appendSocialWorkspaceLinkToMessage(CoreSession documentManager, String contextObject) throws ClientException;
    
    List<DocumentModel> getContextDocuments(CoreSession documentManager, String contextObject) throws ClientException;

    List<String> getContextUsernames(CoreSession documentManager, String contextObject) throws ClientException;
    
}