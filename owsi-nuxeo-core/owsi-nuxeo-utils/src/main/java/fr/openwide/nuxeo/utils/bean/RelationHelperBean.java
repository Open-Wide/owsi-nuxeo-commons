package fr.openwide.nuxeo.utils.bean;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.openwide.nuxeo.utils.document.RelationsHelper;


/**
 * @author mkalam
 *
 */
@Name("relationsHelperBean")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK)
public class RelationHelperBean implements Serializable {
   private static final long serialVersionUID = -2503838501743951894L;
   
   @In
    CoreSession documentManager;
    
    public DocumentModelList getOutgoingRelations(DocumentModel documentModel, String predicate) throws Exception {
        return RelationsHelper.getOutgoingRelations(documentManager, documentModel, predicate);
    }

    public DocumentModelList getIncomingRelations(DocumentModel documentModel, String predicate) throws Exception {
        return RelationsHelper.getIncomingRelations(documentManager, documentModel, predicate);
    }
    
}
