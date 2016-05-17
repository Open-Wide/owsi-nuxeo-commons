package fr.openwide.nuxeo.base.adapters;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PropertyException;

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
    public String getType() {
       return documentModel.getType();
    }

    public String getUuid() {
        return documentModel.getId();
    }

    public String getName() {
        return documentModel.getName();
    }

    public String getTitle() throws NuxeoException {
        return documentModel.getTitle();
    }

    public void setTitle(String title) throws PropertyException, NuxeoException {
        documentModel.setPropertyValue(TypeDocument.XPATH_TITLE, title);
    }

    public String getDescription() throws NuxeoException {
        return (String) documentModel.getPropertyValue(TypeDocument.XPATH_DESCRIPTION);
    }

    public void setDescription(String description) throws PropertyException, NuxeoException {
        documentModel.setPropertyValue(TypeDocument.XPATH_DESCRIPTION, description);
    }

    public String getCreated() throws NuxeoException {
       return (String) documentModel.getPropertyValue(TypeDocument.XPATH_CREATED);
    }
    
    public String getModified() throws NuxeoException {
       return (String) documentModel.getPropertyValue(TypeDocument.XPATH_MODIFIED);
    }

    public Object getProperty(String xpath) throws Exception {
        return documentModel.getPropertyValue(xpath);
    }

    public void setProperty(String xpath, Serializable value) throws Exception {
        documentModel.setPropertyValue(xpath, value);
    }
    
}
