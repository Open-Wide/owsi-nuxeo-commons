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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.NuxeoException;
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

   private static final Log log = LogFactory.getLog(DocumentUtils.class);

   /** for localCopy */
   @SuppressWarnings("serial")
   private static Set<String> DC_PROPERTIES_NOT_TO_COPY = new HashSet<String>() {{
      add("creator");
      add("created");
      add("modified");
      add("contributors");
      add("lastContributor");
   }};
   /** for localCopy */
   @SuppressWarnings("serial")
   private static Set<String> SCHEMAS_NOT_TO_COPY = new HashSet<String>() {{
      add("uid");
      add("dublincore");
      //add("common"); // could allow for custom icon
      //add("webcontainer"); // could allow for ??
      add("publishing");
   }};
   
   
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
     * @throws NuxeoException
     */
    public static DocumentModel createDocument(CoreSession session, String type, String parentPath, String name,
            String title) throws NuxeoException {
        Map<String, Serializable> properties = new HashMap<String, Serializable>();
        // setting title for better document display :
        properties.put(TypeDocument.XPATH_TITLE, title);
        return createDocument(session, type, parentPath, name, properties, null);
    }

    /**
     * Creates a document and attaches some properties.
     * 
     * @param session
     * @param type
     * @param parentPath
     * @param name
     * @param properties
    * @param set 
     * @return
     * @throws NuxeoException
     */
    public static DocumentModel createDocument(CoreSession session, String type, String parentPath, String name,
            Map<String, Serializable> properties, Set<String> facets) throws NuxeoException {
        DocumentModel model = session.createDocumentModel(parentPath, name, type);
        if (facets != null) {
           for (String facet : facets) {
              if (!model.hasFacet(facet)) {
                 model.addFacet(facet); // else PropertyNotFoundException: thumb:thumbnail on docs
              }
           }
        }
        if (properties != null) {
            for (Entry<String, Serializable> property : properties.entrySet()) {
                model.setPropertyValue(property.getKey(), property.getValue());
            }
        }
        if (properties == null || properties.containsKey(TypeDocument.XPATH_TITLE)) {
           // setting title if none for better document display
           model.setPropertyValue(TypeDocument.XPATH_TITLE, name);
        }
        return session.createDocument(model);
    }

    /**
     * Creates a new document that has all all properties except those that must not / can't
     * be copied i.e. DC_PROPERTIES_NOT_TO_COPY dc props and SCHEMAS_NOT_TO_COPY props.
     * NB. for non-customized recursive copy, prefer CoreSession.copy().
     * @param sourceModel
     * @param destinationDocumentModel
     * @param newDocName
     * @return
     * @throws NuxeoException
     */
    public static DocumentModel localCopy(DocumentModel sourceModel, DocumentModel destinationDocumentModel,
          String newDocName) throws NuxeoException {
        CoreSession documentManager = sourceModel.getCoreSession();
        newDocName = (newDocName != null) ? newDocName : sourceModel.getName();
        Map<String, Serializable> properties = copyProperties(sourceModel);
        DocumentModel newProjectElement = DocumentUtils.createDocument(documentManager, sourceModel.getType(),
              destinationDocumentModel.getPathAsString(), newDocName, properties, sourceModel.getFacets());
       return newProjectElement;
    }
    public static Map<String, Serializable> copyProperties(DocumentModel sourceModel) throws NuxeoException {
       Map<String,Serializable> properties = new HashMap<String,Serializable>();
       /*
       schema props for ex. ICP Project :
(uid : id version...)
dublincore : title, description (creator, created, modified, contributors, lastContributor)
files, file
(common : icon...)
icprequireddocuments!
icpphasedata, icpprojectclassificationdata NOT USED
icpapprovabledocument NOT YET
icpassembleddocument!
(webcontainer : webc:logo)
icpphase : used ?
icprequireddocument!
(publishing : sections)
        */
       for (Map.Entry<String, Object> dcPropEntry : sourceModel.getProperties("dublincore").entrySet()) {
          if (DC_PROPERTIES_NOT_TO_COPY.contains(dcPropEntry.getKey())) {
             continue;
          }
          properties.put(dcPropEntry.getKey(), (Serializable) dcPropEntry.getValue());
       }
          
       for (String schema : sourceModel.getSchemas()) {
          if (SCHEMAS_NOT_TO_COPY.contains(schema)) {
             continue;
          }
          //@SuppressWarnings("unchecked")
          //Map<? extends String, ? extends Serializable> schemaProps = (Map<? extends String, ? extends Serializable>) projectModel.getProperties(schema);
          Map<String, Object> schemaProps = (Map<String, Object>) sourceModel.getProperties(schema);
          for(String key : schemaProps.keySet()){
            //properties.putAll(schemaProps);
             properties.put(key, (Serializable) schemaProps.get(key));
          }
       }
       return properties;
    }
    /**
     * RATHER USE CoreSession.copy(), provided only to show how to use localCopy,
     * as a base for customized behaviours
     * @param modelElementDoc
     * @param newProjectInstanceParentElement
     * @param newName
     * @return
     * @throws NuxeoException
     */
    public static DocumentModel recursiveCopy(DocumentModel sourceDoc, DocumentModel destParentDoc, String newName) 
             throws NuxeoException {
          CoreSession documentManager = sourceDoc.getCoreSession();
          DocumentModel destDoc = DocumentUtils.localCopy(
                   sourceDoc, destParentDoc, newName);
          for (DocumentModel modelChildDoc : documentManager.getChildren(sourceDoc.getRef())) {
             recursiveCopy(modelChildDoc, destDoc, null);
          }
          return destDoc;
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

    public static String getDocumentTypeLabel(String doctype) {
        try {
            TypeManager typeManager = Framework.getService(TypeManager.class);
            return typeManager.getType(doctype).getLabel();
        } catch (Exception e) {
            log.warn("Failed to fetch document type label, falling back to type name instead.");
            return doctype;
        }
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
     * @throws NuxeoException
     */
    public static List<String> getDifferingProperties(DocumentModel m1, DocumentModel m2) throws NuxeoException {
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
