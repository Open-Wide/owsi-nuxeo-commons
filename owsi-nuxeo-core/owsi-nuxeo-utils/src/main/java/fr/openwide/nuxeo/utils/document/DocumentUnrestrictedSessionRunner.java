package fr.openwide.nuxeo.utils.document;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;


/**
 * BEWARE :
 * - it changes the user, so to get the actual user name rather use
 * ((NuxeoPrincipal) session.getPrincipal()).getOriginatingUser()
 * - this.doc is NOT updated because in another session, remember to
 * update it (by session.getDocument(doc.getRef()))
 * 
 * @author mdutoo
 *
 */
public abstract class DocumentUnrestrictedSessionRunner extends UnrestrictedSessionRunner {

    private DocumentModel doc;

    protected DocumentUnrestrictedSessionRunner(CoreSession session, DocumentModel doc) {
        super(session);
        this.doc = doc;
    }
    
    /**
     * Use this instead of doc to get the document within the unrestricted session ;
     * NOO still fails, because of session sid in doc ?? so rather give enough permissions
     * (anyway this problem occurs only at permission change level ex. createProjectInstance
     * @return
     * @throws NuxeoException
     */
    public DocumentModel getDocumentModel() throws NuxeoException {
        return session.getDocument(doc.getRef()); // else would use the wrong session within unrestricted
    }
    
    public DocumentModel runUnrestrictedAndGetModel() throws NuxeoException {
        runUnrestricted();
        return session.getDocument(doc.getRef()); // else would use the wrong session within unrestricted
    }

}
