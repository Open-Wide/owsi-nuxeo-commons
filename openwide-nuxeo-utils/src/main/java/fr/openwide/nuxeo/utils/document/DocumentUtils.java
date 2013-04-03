package fr.openwide.nuxeo.utils.document;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.types.TypeDocument;

/**
 * 
 * @author mkalam-alami
 * 
 */
public class DocumentUtils {

    public static final String getRepositoryName() {
        return Framework.isTestModeSet() ? "test" : "default";
    }

    /**
     * @param filename
     * @return The document extension (e.g.: ".docx") or an empty string.
     */
    public static String getFileExtension(String filename) {
        return getFileExtension(filename, "");
    }

    /**
     * @param filename
     * @param defaultValue
     * @return The document extension (e.g.: ".docx") or defaultValue.
     */
    public static String getFileExtension(String filename, String defaultValue) {
        if (filename != null) {
            int lastDot = filename.lastIndexOf(".");
            if (lastDot != -1) {
                return filename.substring(lastDot);
            }
        }
        return defaultValue;
    }

    public static DocumentModel createDocument(CoreSession session, String type,
            String parentPath, String name, String title) throws ClientException {
        Map<String, Serializable> properties = new HashMap<String, Serializable>();
        properties.put(TypeDocument.XPATH_TITLE, title);
        return createDocument(session, type, parentPath, name, properties);
    }

    public static DocumentModel createDocument(CoreSession session, String type, String parentPath, String name,
            Map<String, Serializable> properties) throws ClientException {
        DocumentModel model = session.createDocumentModel(parentPath, name, type);
        if (properties != null) {
            for (Entry<String, Serializable> property : properties.entrySet()) {
                model.setPropertyValue(property.getKey(), property.getValue());
            }
        }
        return session.createDocument(model);
    }

    /**
     * Returns true if and only if the "to" doctype is a supertype of "from"
     * @param from target type
     * @param to queried supertype
     * @return true if to is a supertype of from
     */
    public static boolean isAssignable(String from, String to) {
        if (from.equals(to)) {
            return true;
        }

        TypeManager typeManager;
        try {
            typeManager = Framework.getService(TypeManager.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get TypeManager", e);
        }

        Set<String> superTypes = new HashSet<String>();
        Collections.addAll(superTypes, typeManager.getSuperTypes(from));
        return superTypes.contains(to);
    }
}
