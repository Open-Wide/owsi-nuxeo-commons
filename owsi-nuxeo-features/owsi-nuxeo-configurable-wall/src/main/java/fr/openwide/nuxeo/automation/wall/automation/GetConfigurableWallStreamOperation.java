package fr.openwide.nuxeo.automation.wall.automation;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.activity.ActivityHelper;
import org.nuxeo.ecm.activity.ActivityMessage;
import org.nuxeo.ecm.activity.ActivityReplyMessage;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.InputStreamBlob;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.rating.api.LikeService;
import org.nuxeo.ecm.rating.api.LikeStatus;

import fr.openwide.nuxeo.automation.wall.provider.ConfigurableWallStreamPageProvider;
import fr.openwide.nuxeo.automation.wall.service.ConfigurableWallStreamService;

/**
 * Adapté de {@link GetWallActivityStream}
 * @author mkalam-alami
 *
 */
@Operation(id = GetConfigurableWallStreamOperation.ID, 
    category = "Open Wide",
    label = "Get a wall activity stream",
    description = "Get a wall activity stream for the given document.")
public class GetConfigurableWallStreamOperation {

    public static final String ID = "OpenWide.GetConfigurableWallStream";
    
    @Context
    protected CoreSession session;

    @Context
    protected PageProviderService pageProviderService;

    @Context
    protected ConfigurableWallStreamService configurableWallStreamService;

    @Context
    protected LikeService likeService;

    @Param(name = "activityStreamName", required = false)
    protected String activityStreamName;

    @Param(name = "activityLinkBuilder", required = false)
    protected String activityLinkBuilder;

    @Param(name = "context", required = true)
    protected String context;

    @Param(name = "language", required = false)
    protected String language;

    @Param(name = "offset", required = false)
    protected Integer offset;

    @Param(name = "limit", required = false)
    protected Integer limit;

    @OperationMethod
    public Blob run() throws Exception {
        Long targetOffset = 0L;
        if (offset != null) {
            targetOffset = offset.longValue();
        }
        Long targetLimit = null;
        if (limit != null) {
            targetLimit = limit.longValue();
        }

        if (StringUtils.isBlank(activityStreamName)) {
            // assume we are on a Social workspace
            activityStreamName = "socialWorkspaceActivityStream";
        }

        Locale locale = language != null && !language.isEmpty() ? new Locale(language) : Locale.ENGLISH;

        // Select configuration handler
        List<ConfigurableWallStreamHandler> handlers = configurableWallStreamService.getConfigurableWallStreamHandlers();
        ConfigurableWallStreamHandler activeHandler = null;
        for (ConfigurableWallStreamHandler handler : handlers) {
            if (handler.accept(session, context)) {
                activeHandler = handler;
                break;
            }
        }
        
        
        Map<String, Serializable> props = new HashMap<String, Serializable>();
        props.put(ConfigurableWallStreamPageProvider.ACTIVITY_STREAM_NAME_PROPERTY, activityStreamName);
        props.put(ConfigurableWallStreamPageProvider.ACTIVITY_LINK_BUILDER_NAME_PROPERTY, activityLinkBuilder);
        props.put(ConfigurableWallStreamPageProvider.LOCALE_PROPERTY, locale);
        props.put(ConfigurableWallStreamPageProvider.CORE_SESSION_PROPERTY, (Serializable) session);
        props.put(ConfigurableWallStreamPageProvider.CONTEXT_DOCUMENTS_PROPERTY,
                handleContextDocuments(session, context, activeHandler));
        props.put(ConfigurableWallStreamPageProvider.CONTEXT_USER_PROPERTY,
                handleContextUser(session, context, activeHandler));
        if (activeHandler != null && activeHandler.appendSocialWorkspaceLinkToMessage(session, context)) {
            props.put(ConfigurableWallStreamPageProvider.APPEND_SOCIAL_WORKSPACE_LINK_PROPERTY, true);
        }
        
        @SuppressWarnings("unchecked")
        PageProvider<ActivityMessage> pageProvider = (PageProvider<ActivityMessage>) pageProviderService.getPageProvider(
                ConfigurableWallStreamPageProvider.NAME, null, targetLimit, 0L, props);
        pageProvider.setCurrentPageOffset(targetOffset);

        List<ActivityMessage> activityMessages = pageProvider.getCurrentPage();
        List<Map<String, Object>> activitiesJSON = new ArrayList<Map<String, Object>>();
        for (ActivityMessage activityMessage : activityMessages) {
            Map<String, Object> o = activityMessage.toMap(session, locale,
                    activityLinkBuilder);
            o.put("replies",
                    toActivityReplyMessagesJSON(session, locale,
                            activityMessage.getActivityReplyMessages()));
            if (activityMessage.getVerb().equals("minimessage")) {
                o.put("allowDeletion",
                        getAllowDeletion(activityMessage.getActor()));
            }
            o.put("likeStatus", getLikeStatus(activityMessage.getActivityId()));

            // Date plus détaillée
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, locale);
            o.put("publishedDate", dateFormat.format(activityMessage.getPublishedDate()));
            
            activitiesJSON.add(o);
        }

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("offset", ((ConfigurableWallStreamPageProvider) pageProvider).getNextOffset());
        m.put("limit", pageProvider.getPageSize());
        m.put("activities", activitiesJSON);

        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, m);

        return new InputStreamBlob(new ByteArrayInputStream(
                writer.toString().getBytes("UTF-8")), "application/json");
    }

    private Serializable handleContextDocuments(CoreSession session, String context,
            ConfigurableWallStreamHandler activeHandler) throws ClientException {
        if (activeHandler != null) {
            return (Serializable) activeHandler.getContextDocuments(session, context);
        }
        
        // Context path // TODO Put in handler
        PathRef docRef = new PathRef(context);
        if (session.exists(docRef)) {
            return session.getDocument(docRef);
        }
        return null;
    }
    
    private Serializable handleContextUser(CoreSession session, String context,
            ConfigurableWallStreamHandler activeHandler) throws ClientException {
        if (activeHandler != null) {
            return (Serializable) activeHandler.getContextUsernames(session, context);
        }
        return null;
    }

    private List<Map<String, Object>> toActivityReplyMessagesJSON(
            CoreSession session, Locale locale,
            List<ActivityReplyMessage> activityReplyMessages)
            throws ClientException {
        List<Map<String, Object>> replies = new ArrayList<Map<String, Object>>();
        for (ActivityReplyMessage activityReplyMessage : activityReplyMessages) {
            Map<String, Object> o = activityReplyMessage.toMap(session, locale,
                    activityLinkBuilder);
            o.put("allowDeletion",
                    getAllowDeletion(activityReplyMessage.getActor()));
            o.put("likeStatus",
                    getLikeStatus(activityReplyMessage.getActivityReplyId()));
            
            // Date plus détaillée // TODO Make configurable by handler
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, locale);
            o.put("publishedDate", dateFormat.format(activityReplyMessage.getPublishedDate()));
            
            replies.add(o);
        }
        return replies;
    }

    private boolean getAllowDeletion(String actor) {
        String actorUsername = ActivityHelper.getUsername(actor);
        return session.getPrincipal().getName().equals(actorUsername);
    }

    private Map<String, Serializable> getLikeStatus(Serializable id) {
        String activityObject = ActivityHelper.createActivityObject(id);
        LikeStatus likeStatus = likeService.getLikeStatus(
                session.getPrincipal().getName(), activityObject);
        return likeStatus.toMap();
    }

}
