package fr.openwide.nuxeo.avatar;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.runtime.api.Framework;

/**
 * 
 * @author mkalam-alami
 *
 */
public class AvatarImporterScheduleRunner extends UnrestrictedSessionRunner {

    public static Logger logger = Logger.getLogger(AvatarImporterScheduleRunner.class);
    
    protected AvatarImporterScheduleRunner(String repositoryName, String originatingUser) {
        super(repositoryName, originatingUser);
    }

    @Override
    public void run() throws NuxeoException {
        try {
            AvatarImporterService avatarsService = Framework.getService(AvatarImporterService.class);
            avatarsService.importAvatars(session);
        } catch (Exception e) {
            throw new NuxeoException(e);
        }
    }

}
