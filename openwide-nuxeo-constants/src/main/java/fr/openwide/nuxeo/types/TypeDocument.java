package fr.openwide.nuxeo.types;

/**
 * 
 * @author mkalam-alami
 *
 */
public interface TypeDocument {

    static final String TYPE = "Document";
    
    static final String XPATH_TITLE = "dc:title";
    
    static final String XPATH_MODIFIED = "dc:modified";

    static final String XPATH_DESCRIPTION = "dc:description";
    
    /**
     * Stocke un path (ex: "/img/EQS.png")
     */
    static final String XPATH_ICON = "icon";
    
}
