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
package fr.openwide.nuxeo.test;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;


/**
 * Debugging tool that traces document structures
 * @author mkalam-alami
 *
 */
public class RepositoryLogger {

    private static final int INDENT_STEP = 2; // in spaces

    private static Logger logger = Logger.getLogger(RepositoryLogger.class);
    
    private CoreSession documentManager;
    private final Level level;
    
    private RepositoryLoggerMatcher matcher = new RepositoryLoggerMatcher() {
        @Override
        public boolean matches(DocumentModel model) {
            return false;
        }
    };

    public RepositoryLogger(CoreSession documentManager) {
        this(documentManager, Level.DEBUG);
    }
    
    public RepositoryLogger(CoreSession documentManager, Level level) {
        this.documentManager = documentManager;
        this.level = level;
    }

    /**
     * Allows to define a matcher to set which documents
     * need to be logged in details.
     */
    public RepositoryLogger enableDetailedLoggingFor(RepositoryLoggerMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public void logAllRepository() {
        logAllRepository("Repository contents");
    }
    
    public void logAllRepository(String title) {
        try {
            logHeader(title);
            DocumentModelList domains = documentManager.getChildren(new PathRef("/"));
            for (DocumentModel domain : domains) {
                logDocumentAndChildren(domain, null);
            }
        } catch (NuxeoException e) {
            logger.log(level, "ERROR: Failed to log a document", e);
        }
    }

    public void logDocumentAndChildren(DocumentModel model) throws NuxeoException {
        logDocumentAndChildren(model, model.getTitle());
    }
    
    public void logDocumentAndChildren(DocumentModel model, String title) {
        if (logger.isEnabledFor(level)) {
            try {
                // Header
                if (title != null) {
                    logHeader(title);
                }
                // Contents
                logDocumentAndChildren(model, 0);
            } catch (NuxeoException e) {
                logger.log(level, "ERROR: Failed to log document or a document child", e);
            }
        }
    }
    
    private void logDocumentAndChildren(DocumentModel model, int indent) throws NuxeoException {
        // Log document
        if (matcher.matches(model)) {
            logDetailed(indent, model);
        }
        else {
            logBasic(indent, model);
        }
        
        // Recursive calls
        DocumentModelList list = documentManager.getChildren(model.getRef());
        for (DocumentModel childModel : list) {
            logDocumentAndChildren(childModel, indent+INDENT_STEP);
        }
    }

    private void logBasic(int indent, DocumentModel model) {
        String type = model.getType();
        if (model.getDocumentType().getFacets().contains("SoaNode")) {
            type += ":" + model.getName();
        }
        String line = getSpaces(indent) + "* [" + type + "] ";
        try {
            line += model.getTitle();
        } catch (NuxeoException e) {
            line += "<title unknown>";
        }
        logger.log(level, line);
    }

    private void logDetailed(int indent, DocumentModel model) {
        logBasic(indent, model);
        String spaces = getSpaces(indent);
        try {
            for (String schema : model.getDocumentType().getSchemaNames()) {
                StringBuffer line = new StringBuffer(spaces + "    | " + schema + "> ");
                Map<String, Object> schemaProperties = model.getProperties(schema);
                for (Entry<String, Object> entry : schemaProperties.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof GregorianCalendar) {
                        value = ((GregorianCalendar) value).getTime().toString();
                    }
                    if (value instanceof String[]) {
                        StringBuilder newValue = new StringBuilder();
                        for (String valueString : (String[]) value) {
                            newValue.append(valueString);
                            newValue.append(',');
                        }
                        value = newValue.toString();
                    }
                    line.append(entry.getKey() + "=" + value + " ");
                }
                logger.log(level, line.toString());
            }
        }
        catch(Exception e) {
            logger.log(level, spaces + "(Failed to get more information: " + e.getMessage() + ")");
        }
    }
    
    private void logHeader(String title) {
        String separator = getDashes(title.length());
        logger.log(level, separator);
        logger.log(level, title);
        logger.log(level, separator);
    }

    private String getDashes(int length) {
        return getCharSuite('-', length);
    }
    
    private String getSpaces(int length) {
        return getCharSuite(' ', length);
    }
    
    private String getCharSuite(char c, int length) {
        StringBuffer line = new StringBuffer();
        for (int i = 0; i< length; i++) {
            line.append(c);
        }
        return line.toString();
    }
    
}
