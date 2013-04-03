package fr.openwide.nuxeo.avatar;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;

import fr.openwide.nuxeo.utils.document.DocumentUtils;

/**
 * 
 * @author mkalam-alami
 *
 */
public class AvatarImporterSchedule implements EventListener {

    public static Logger logger = Logger.getLogger(AvatarImporterSchedule.class);
    
    private static boolean schedulerLock = false; 

    
    public void handleEvent(Event event) throws ClientException {
        if (!schedulerLock) {
            schedulerLock = true;
            try {
                new AvatarImporterScheduleRunner(DocumentUtils.getRepositoryName(),
                        event.getContext().getPrincipal().getName())
                    .runUnrestricted();
            } catch (Exception e) {
                logger.error("Error while importing the avatars", e);
            }
            schedulerLock = false;
        }
    }
    
}
