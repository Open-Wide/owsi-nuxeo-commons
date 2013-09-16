
package fr.openwide.nuxeo.automation.wall.automation;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.social.workspace.adapters.SocialWorkspace;

/**
 * @author mkalam-alami
 *
 */
public class UserSocialWorkspacesStreamHandler implements ConfigurableWallStreamHandler {

    public static final String CONTEXT_ID = "OpenWide:UserSocialWorkspacesStream";
    
    private static final String QUERY_SOCIAL_WORKSPACES = "SELECT * FROM SocialWorkspace WHERE " 
            + NXQL.ECM_LIFECYCLESTATE + " != 'deleted'";
    
    /* (non-Javadoc)
     * @see fr.openwide.nuxeo.automation.wall.ConfigurableWallStreamHandler#accept(org.nuxeo.ecm.core.api.CoreSession, java.lang.String)
     */
    public boolean accept(CoreSession documentManager, String contextObject) {
        return CONTEXT_ID.equals(contextObject);
    }

    /* (non-Javadoc)
     * @see fr.openwide.nuxeo.automation.wall.ConfigurableWallStreamHandler#appendSocialWorkspaceLinkToMessage(org.nuxeo.ecm.core.api.CoreSession, java.lang.String)
     */
    public boolean appendSocialWorkspaceLinkToMessage(CoreSession documentManager, String contextObject) {
        return true;
    }

    /* (non-Javadoc)
     * @see fr.openwide.nuxeo.automation.wall.ConfigurableWallStreamHandler#getContextDocuments(org.nuxeo.ecm.core.api.CoreSession, java.lang.String)
     */
    public List<DocumentModel> getContextDocuments(CoreSession documentManager, String contextObject) throws ClientException {
        List<DocumentModel> contextDocuments = new ArrayList<DocumentModel>();
        Principal principal = documentManager.getPrincipal();
        
        if (principal instanceof NuxeoPrincipal) {
            NuxeoPrincipal nuxeoPrincipal = (NuxeoPrincipal) principal;
            DocumentModelList socialWorkspaceModels = documentManager.query(QUERY_SOCIAL_WORKSPACES); // TODO Cache
            for (DocumentModel socialWorkspaceModel : socialWorkspaceModels) {
                SocialWorkspace socialWorkspace = socialWorkspaceModel.getAdapter(SocialWorkspace.class);
                if (socialWorkspace.isMember(nuxeoPrincipal)) {
                    contextDocuments.add(socialWorkspace.getDocument());
                }
            }
        }
        
        return contextDocuments;
    }

    /* (non-Javadoc)
     * @see fr.openwide.nuxeo.automation.wall.ConfigurableWallStreamHandler#getContextUsernames(org.nuxeo.ecm.core.api.CoreSession, java.lang.String)
     */
    public List<String> getContextUsernames(CoreSession documentManager, String contextObject) {
        return null;
    }
    
}
