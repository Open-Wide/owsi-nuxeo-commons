package fr.openwide.nuxeo.base.adapters;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import fr.openwide.nuxeo.types.TypeDocument;

/**
 * 
 * @author mkalam-alami
 *
 */
public class DocumentAdapter implements TypeDocument {

    ///private static Logger logger = Logger.getLogger(BaseTypesAdapterFactory.class);
    
    protected final DocumentModel documentModel;

    public DocumentAdapter(DocumentModel documentModel) {
        this.documentModel = documentModel;

        // TODO LATER handle copied properties / inherited facets, or in extending abstract adapter...
    }
    
    public DocumentModel getDocumentModel() {
        return documentModel;
    }

    /** may be overriden (but there should be no need ??) */
    @Override
    public String getType() {
       return documentModel.getType();
    }

    @Override
    public String getUuid() {
        return documentModel.getId();
    }

    @Override
    public String getName() {
        return documentModel.getName();
    }

    @Override
    public String getTitle() throws ClientException {
        return documentModel.getTitle();
    }

    @Override
    public void setTitle(String title) throws PropertyException, ClientException {
        documentModel.setPropertyValue(TypeDocument.XPATH_TITLE, title);
    }

    @Override
    public String getDescription() throws ClientException {
        return (String) documentModel.getPropertyValue(TypeDocument.XPATH_DESCRIPTION);
    }

    @Override
    public void setDescription(String description) throws PropertyException, ClientException {
        documentModel.setPropertyValue(TypeDocument.XPATH_DESCRIPTION, description);
    }


    @Override
    public String getCreated() throws ClientException {
       return (String) documentModel.getPropertyValue(TypeDocument.XPATH_CREATED);
    }
    

    @Override
    public String getModified() throws ClientException {
       return (String) documentModel.getPropertyValue(TypeDocument.XPATH_MODIFIED);
    }

    @Override
    public Object getProperty(String xpath) throws Exception {
        return documentModel.getPropertyValue(xpath);
    }

    @Override
    public void setProperty(String xpath, Serializable value) throws Exception {
        documentModel.setPropertyValue(xpath, value);
    }
    
}
