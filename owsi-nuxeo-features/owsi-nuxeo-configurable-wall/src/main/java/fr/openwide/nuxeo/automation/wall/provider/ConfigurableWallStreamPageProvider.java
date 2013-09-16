package fr.openwide.nuxeo.automation.wall.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.activity.AbstractActivityPageProvider;
import org.nuxeo.ecm.activity.ActivitiesList;
import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityHelper;
import org.nuxeo.ecm.activity.ActivityLinkBuilder;
import org.nuxeo.ecm.activity.ActivityMessage;
import org.nuxeo.ecm.activity.ActivityStreamService;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.runtime.api.Framework;



/**
 * D'après {@link WallActivityStreamPageProvider}
 * 
 * @author mkalam-alami
 *
 */
public class ConfigurableWallStreamPageProvider extends AbstractActivityPageProvider<ActivityMessage> {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(ConfigurableWallStreamPageProvider.class);
    
    public static final String NAME = "configurableWallStreamProvider";
    
    public static final String ACTIVITY_STREAM_NAME_PROPERTY = "activityStreamName";

    public static final String ACTIVITY_LINK_BUILDER_NAME_PROPERTY = "activityLinkBuilderName";

    public static final String CONTEXT_DOCUMENTS_PROPERTY = "contextDocuments";
    
    public static final String CONTEXT_USER_PROPERTY = "contextUser";

    public static final String LOCALE_PROPERTY = "locale";

    public static final String CORE_SESSION_PROPERTY = "coreSession";

    public static final String APPEND_SOCIAL_WORKSPACE_LINK_PROPERTY = "appendSocialWorkspaceLink";

    protected List<ActivityMessage> pageActivityMessages;

    @Override
    public List<ActivityMessage> getCurrentPage() {
        if (pageActivityMessages == null) {
            pageActivityMessages = new ArrayList<ActivityMessage>();
            long pageSize = getMinMaxPageSize();

            ActivityStreamService activityStreamService = Framework.getLocalService(ActivityStreamService.class);
            Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            parameters.put(ConfigurableWallStreamFilter.ACTIVITY_STREAM_PARAMETER, getActivityStreamName());
            parameters.put(ConfigurableWallStreamFilter.CONTEXT_DOCUMENTS_PARAMETER, getContextDocuments());
            parameters.put(ConfigurableWallStreamFilter.CONTEXT_USER_PARAMETER, getContextUser());
            ActivitiesList activities = activityStreamService.query(ConfigurableWallStreamFilter.ID, parameters,
                    getCurrentPageOffset(), pageSize);
            nextOffset = offset + activities.size();
            activities = activities.filterActivities(getCoreSession());
            
            // Rajouter le titre du social workspace en fin de message
            if (properties.containsKey(APPEND_SOCIAL_WORKSPACE_LINK_PROPERTY)) {
                CoreSession coreSession = getCoreSession();
                Locale locale = getLocale();
                String activityLinkBuilderName = getActivityLinkBuilderName();
                ActivityLinkBuilder activityLinkBuilder = activityStreamService.getActivityLinkBuilder(activityLinkBuilderName);
                Map<String, String> contextTitles = new HashMap<String, String>();
                for (Activity activity : activities) {
                    try {
                        String documentId = null;
                        if (ActivityHelper.isUser(activity.getContext())) {
                            String activityObjectId = ActivityHelper.getDocumentId(activity.getObject());
                            List<DocumentModel> parents = coreSession.getParentDocuments(new IdRef(activityObjectId));
                            for (DocumentModel parent : parents) {
                                if ("Affaire".equals(parent.getType()) || parent.hasFacet("SocialWorkspace")) {
                                    documentId = parent.getId();
                                    activity.setTarget(ActivityHelper.createDocumentActivityObject(parent));
                                    contextTitles.put(documentId, parent.getTitle().trim());
                                }
                            }
                        }
                        else {
                            documentId = ActivityHelper.getDocumentId(activity.getContext());
                        }
                        if (!contextTitles.containsKey(documentId)) {
                                contextTitles.put(documentId, coreSession.getDocument(new IdRef(documentId)).getTitle().trim());
                        }
                        ActivityMessage activityMessage = activityStreamService.toActivityMessage(activity, locale, activityLinkBuilderName);
                        pageActivityMessages.add(
                                ActivityMessageHelper.appendContextLinkToMessage(activity, activityMessage,
                                        activityLinkBuilder, contextTitles.get(documentId)));
                    } catch (ClientException e) {
                        logger.error(e);
                    }
                }
            }
            else {
                pageActivityMessages.addAll(activities.toActivityMessages(getLocale(), getActivityLinkBuilderName()));
            }
            
            setResultsCount(UNKNOWN_SIZE_AFTER_QUERY);
        }
        return pageActivityMessages;
    }

    protected String getActivityStreamName() {
        Map<String, Serializable> props = getProperties();
        String activityStreamName = (String) props.get(ACTIVITY_STREAM_NAME_PROPERTY);
        if (activityStreamName == null) {
            throw new ClientRuntimeException("Cannot find " + ACTIVITY_STREAM_NAME_PROPERTY + " property.");
        }
        return activityStreamName;
    }

    protected Serializable getContextDocuments() {
        Map<String, Serializable> props = getProperties();
        Object contextDocumentsObject = props.get(CONTEXT_DOCUMENTS_PROPERTY);
        if (contextDocumentsObject == null) {
            return null;
        }
        else if (contextDocumentsObject instanceof DocumentModel) {
            DocumentModelListImpl contextDocuments = new DocumentModelListImpl();
            contextDocuments.add((DocumentModel) contextDocumentsObject);
            return contextDocuments;
        }
        else if (contextDocumentsObject instanceof List<?>) {
            return (Serializable) contextDocumentsObject;
        }
        else {
            throw new ClientRuntimeException("Invalid " + CONTEXT_DOCUMENTS_PROPERTY + " property.");
        }
    }

    protected Serializable getContextUser() {
        return (String) getProperties().get(CONTEXT_USER_PROPERTY);
	}

	protected Locale getLocale() {
        Map<String, Serializable> props = getProperties();
        Locale locale = (Locale) props.get(LOCALE_PROPERTY);
        if (locale == null) {
            throw new ClientRuntimeException("Cannot find " + LOCALE_PROPERTY + " property.");
        }
        return locale;
    }

    protected String getActivityLinkBuilderName() {
        Map<String, Serializable> props = getProperties();
        return (String) props.get(ACTIVITY_LINK_BUILDER_NAME_PROPERTY);
    }

    protected CoreSession getCoreSession() {
        Map<String, Serializable> props = getProperties();
        CoreSession session = (CoreSession) props.get(CORE_SESSION_PROPERTY);
        if (session == null) {
            throw new ClientRuntimeException("Cannot find " + CORE_SESSION_PROPERTY + " property.");
        }
        return session;
    }

    @Override
    public boolean isSortable() {
        return false;
    }

    @Override
    protected void pageChanged() {
        super.pageChanged();
        pageActivityMessages = null;
    }

    @Override
    public void refresh() {
        super.refresh();
        pageActivityMessages = null;
    }

}
