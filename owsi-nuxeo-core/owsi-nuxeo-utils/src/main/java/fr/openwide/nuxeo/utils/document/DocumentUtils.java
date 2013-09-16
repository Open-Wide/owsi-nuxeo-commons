/*******************************************************************************
 * (C) Copyright 2013 Open Wide (http://www.openwide.fr/) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 ******************************************************************************/
package fr.openwide.nuxeo.utils.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    /**
     * @return The name of the the default repository, in a way that also works
     *         for tests.
     */
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

    /**
     * Creates a document with a title.
     * 
     * @param session
     * @param type
     * @param parentPath
     * @param name
     * @param title
     * @return
     * @throws ClientException
     */
    public static DocumentModel createDocument(CoreSession session, String type, String parentPath, String name,
            String title) throws ClientException {
        Map<String, Serializable> properties = new HashMap<String, Serializable>();
        properties.put(TypeDocument.XPATH_TITLE, title);
        return createDocument(session, type, parentPath, name, properties);
    }

    /**
     * Creates a document and attaches some properties.
     * 
     * @param session
     * @param type
     * @param parentPath
     * @param name
     * @param properties
     * @return
     * @throws ClientException
     */
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
     * 
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

    /**
     * Formats the given string to make it usable as a file name
     * 
     * @param string
     * @return
     */
    public static String toValidFilename(String string) {
        return string.replaceAll("[<>\"\\\\|?!:/%*]", "-");
    }

    /**
     * Returns the properties that differ between two versions of a document.
     * - If a schema is missing from a model, all of its properties will be returned
     * - Only basic types and string arrays as supported. More complex properties will always be considered modified. 
     * 
     * @param m1
     * @param m2
     * @return A list of xpaths
     * @throws ClientException
     */
    public static List<String> getDifferingProperties(DocumentModel m1, DocumentModel m2) throws ClientException {
        List<String> differingProperties = new ArrayList<String>();

        Set<String> schemas = new HashSet<String>();
        schemas.addAll(Arrays.asList(m1.getSchemas()));
        schemas.addAll(Arrays.asList(m2.getSchemas()));
        
        for (String schema : schemas) {
            if (m1.hasSchema(schema)) {
                if (m2.hasSchema(schema)) {
                    Map<String, Object> p1 = m1.getProperties(schema);
                    Map<String, Object> p2 = m2.getProperties(schema);
                    for (Entry<String, Object> property : p1.entrySet()) {
                        boolean differs = false;
                        Object value1 = property.getValue();
                        Object value2 = p2.get(property.getKey());
                        if (value1 == null) {
                            differs = value2 != null;
                        } else {
                            if (value1 instanceof String[]) {
                                differs = !Arrays.equals((String[]) value1, (String[]) value2);
                            } else {
                                differs = !value1.equals(value2);
                            }
                        }
                        if (differs) {
                            differingProperties.add(property.getKey());
                        }
                    }
                } else {
                    differingProperties.addAll(m1.getProperties(schema).keySet());
                }
            } else {
                differingProperties.addAll(m2.getProperties(schema).keySet());
            }
        }
        
        return differingProperties;
    }
}
