package fr.openwide.nuxeo.automation.wall.provider;

import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityHelper;
import org.nuxeo.ecm.activity.ActivityLinkBuilder;
import org.nuxeo.ecm.activity.ActivityMessage;

public class ActivityMessageHelper {

    public static ActivityMessage appendContextLinkToMessage(Activity activity,
            ActivityMessage message, ActivityLinkBuilder linkBuilder, String linkTitle) {

        String targetSpaceObject;
        if (ActivityHelper.isUser(activity.getContext())) {
            targetSpaceObject = activity.getTarget();
        }
        else {
            targetSpaceObject = activity.getContext();
        }
        
        String lienSocialWorkspace = " <span style=\"font-size: 80%; color: #555\">"
                + "(on " // TODO i18n
                + linkBuilder.getDocumentLink(targetSpaceObject, linkTitle)
                        .replace("a href", "a style=\"color: #555\" href")
                + ")";
        return new ActivityMessage(activity.getId(),
                message.getActor(),
                message.getDisplayActor(),
                message.getDisplayActorLink(),
                message.getVerb(),
                message.getMessage() + lienSocialWorkspace,
                message.getPublishedDate(),
                message.getIcon(),
                message.getActivityReplyMessages());
    }
    
}
