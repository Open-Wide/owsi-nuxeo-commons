package fr.openwide.nuxeo.base.adapters;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.openwide.nuxeo.types.TypeDocument;


/**
 * To subclass adapters in other projects, let their factory replace this one
 * for the subclassed interface and register it after this one.
 *  NB. can't support more than one adapter subclassing project !! TODO LATER better 
 * 
 * @author mkalam-alami, mdutoo
 *
 */
public class BaseTypesAdapterFactory implements DocumentAdapterFactory {
    
    protected static Logger logger = Logger.getLogger(BaseTypesAdapterFactory.class);

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        try {
            if (TypeDocument.class.equals(itf)) {
                return new DocumentAdapter(doc);
            }
        }
        catch (Exception e) {
            logger.warn("Could not create adapter: " + e.getMessage());
        }
        return null;
    }
    
}
