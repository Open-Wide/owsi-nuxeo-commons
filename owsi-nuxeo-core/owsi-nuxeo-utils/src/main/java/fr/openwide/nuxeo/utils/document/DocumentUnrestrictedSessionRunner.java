package fr.openwide.nuxeo.utils.document;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

public abstract class DocumentUnrestrictedSessionRunner extends UnrestrictedSessionRunner {

    protected DocumentModel doc;

    protected DocumentUnrestrictedSessionRunner(CoreSession session, DocumentModel doc) {
        super(session);
        this.doc = doc;
    }
    
    public DocumentModel getDocumentModel() {
        return doc;
    }
    
    public DocumentModel runUnrestrictedAndGetModel() throws ClientException {
        runUnrestricted();
        return doc;
    }

}
