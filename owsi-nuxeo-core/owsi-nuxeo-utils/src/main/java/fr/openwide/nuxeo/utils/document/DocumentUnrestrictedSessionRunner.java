package fr.openwide.nuxeo.utils.document;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

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
     * @throws ClientException
     */
    public DocumentModel getDocumentModel() throws ClientException {
        return session.getDocument(doc.getRef()); // else would use the wrong session within unrestricted
    }
    
    public DocumentModel runUnrestrictedAndGetModel() throws ClientException {
        runUnrestricted();
        return session.getDocument(doc.getRef()); // else would use the wrong session within unrestricted
    }

}
