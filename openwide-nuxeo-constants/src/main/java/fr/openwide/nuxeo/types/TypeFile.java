package fr.openwide.nuxeo.types;

public interface TypeFile extends TypeDocument {

    static final String TYPE = "File";

    static final String SCHEMA_FILE = "file";
    
    static final String XPATH_FILENAME = SCHEMA_FILE + ":filename";
    
    static final String XPATH_CONTENT = SCHEMA_FILE + ":content";
    
}
