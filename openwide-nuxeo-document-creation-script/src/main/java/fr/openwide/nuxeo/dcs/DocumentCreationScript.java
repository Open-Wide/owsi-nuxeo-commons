package fr.openwide.nuxeo.dcs;

import java.security.InvalidParameterException;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.dcs.service.DocumentCreationDescriptor;

/**
 * 
 * @author mkalam-alami
 *
 */
public interface DocumentCreationScript {

    String getName();
    
    void run(CoreSession session, boolean overwrite) throws ClientException;
    
    void run(CoreSession session, DocumentModel context, boolean overwrite) throws ClientException;

    void appendDocumentCreation(DocumentCreationDescriptor descriptor) throws InvalidParameterException;

}
