package fr.openwide.nuxeo.avatar;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * @author mkalam-alami
 *
 */
public interface AvatarImporterService {

    void registerConfiguration(AvatarImporterConfigDescriptor config);
    
    public String getAvatarFolderPath();

    public void importAvatars(CoreSession coreSession) throws NuxeoException;

}
