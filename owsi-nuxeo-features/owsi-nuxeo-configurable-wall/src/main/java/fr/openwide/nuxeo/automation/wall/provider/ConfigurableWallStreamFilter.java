package fr.openwide.nuxeo.automation.wall.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.nuxeo.ecm.activity.ActivitiesList;
import org.nuxeo.ecm.activity.ActivitiesListImpl;
import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityHelper;
import org.nuxeo.ecm.activity.ActivityReply;
import org.nuxeo.ecm.activity.ActivityStream;
import org.nuxeo.ecm.activity.ActivityStreamFilter;
import org.nuxeo.ecm.activity.ActivityStreamService;
import org.nuxeo.ecm.activity.ActivityStreamServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * D'après {@link WallActivityStreamFilter}
 * 
 * @author mkalam-alami
 *
 */
public class ConfigurableWallStreamFilter implements ActivityStreamFilter {

    public static final String ID = "configurableWallStreamFilter";

    public static final String DEFAULT_WALL_ACTIVITY_STREAM_NAME = "defaultWallActivityStream";

    public static final String CONTEXT_DOCUMENTS_PARAMETER = "contextDocumentsParameter";
    
    public static final String CONTEXT_USER_PARAMETER = "contextUserParameter";

    public static final String ACTIVITY_STREAM_PARAMETER = "activityStreamParameter";

    public String getId() {
        return ID;
    }

    public boolean isInterestedIn(Activity activity) {
        return false;
    }

    public void handleNewActivity(ActivityStreamService activityStreamService, Activity activity) {
        // Nothing
    }

    @Deprecated
    public void handleRemovedActivities(ActivityStreamService activityStreamService,
            Collection<Serializable> activityIds) {
        // Nothing
    }

    public void handleRemovedActivities(ActivityStreamService activityStreamService, ActivitiesList activities) {
        // Nothing
    }

    public void handleRemovedActivityReply(ActivityStreamService activityStreamService, Activity activity,
            ActivityReply activityReply) {
        // Nothing
    }

    @SuppressWarnings("unchecked")
    public ActivitiesList query(ActivityStreamService activityStreamService, Map<String, Serializable> parameters,
            long offset, long limit) {
        
        List<String> activityContexts = new ArrayList<String>();
        
        // Listing des contextes
        List<DocumentModel> docs = (List<DocumentModel>) parameters.get(CONTEXT_DOCUMENTS_PARAMETER);
        if (docs != null) {
            for (DocumentModel doc : docs) {
            	activityContexts.add(ActivityHelper.createDocumentActivityObject(doc));
            }
        }
        String userContext = (String) parameters.get(CONTEXT_USER_PARAMETER);
        if (userContext != null && !userContext.isEmpty()) {
        	activityContexts.add(ActivityHelper.createUserActivityObject(userContext));
        }
        
        // Création de la query
        if (!activityContexts.isEmpty()) {
    
            String activityStreamName = (String) parameters.get(ACTIVITY_STREAM_PARAMETER);
            if (activityStreamName == null) {
                activityStreamName = DEFAULT_WALL_ACTIVITY_STREAM_NAME;
            }
            ActivityStream wallActivityStream = activityStreamService.getActivityStream(activityStreamName);
            List<String> verbs = wallActivityStream.getVerbs();
    
            EntityManager em = ((ActivityStreamServiceImpl) activityStreamService).getEntityManager();
            Query query = em.createQuery("select activity from Activity activity " + "where activity.context in (:context) "
                    + "and activity.verb in (:verbs) " + "and activity.actor like :actor "
                    + "order by activity.lastUpdatedDate desc");
            query.setParameter("context", activityContexts);
            query.setParameter("verbs", verbs);
            query.setParameter("actor", "user:%");
    
            if (limit > 0) {
                query.setMaxResults((int) limit);
                if (offset > 0) {
                    query.setFirstResult((int) offset);
                }
            }
            
            // Query
            return new ActivitiesListImpl(query.getResultList());
        }
        else {
        	// Ne pas renvoyer d'erreur
            // (Peut arriver s'il n'y a, par exemple, aucune affaire favorite sélectionnée)
            return new ActivitiesListImpl();
        }
    }
}
