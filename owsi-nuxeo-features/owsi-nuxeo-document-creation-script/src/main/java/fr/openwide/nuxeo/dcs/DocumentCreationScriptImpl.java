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
package fr.openwide.nuxeo.dcs;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.dcs.service.ACEDescriptor;
import fr.openwide.nuxeo.dcs.service.DocumentCreationDescriptor;
import fr.openwide.nuxeo.dcs.service.PropertyDescriptor;

/**
 * 
 * @author mkalam-alami
 *
 */
public class DocumentCreationScriptImpl implements DocumentCreationScript {

    public static Logger logger = Logger.getLogger(DocumentCreationScriptImpl.class);
    
    private List<DocumentCreationDescriptor> creationDescriptors = new ArrayList<DocumentCreationDescriptor>();

    private final String scriptName;
    
    public DocumentCreationScriptImpl(String scriptName) {
        this.scriptName = scriptName;
    }

    public DocumentCreationScriptImpl(String scriptName, List<DocumentCreationDescriptor> creationDescriptors) {
        this.scriptName = scriptName;
        this.creationDescriptors = creationDescriptors;
    }
    
    @Override
    public void appendDocumentCreation(DocumentCreationDescriptor descriptor) throws InvalidParameterException {
        if (descriptor.path == null && descriptor.title == null) {
            throw new InvalidParameterException("Path and title can't be both null");
        }
        creationDescriptors.add(descriptor);
    }

    @Override
    public void run(CoreSession session, boolean overwrite) throws NuxeoException {
        run(session, session.getRootDocument(), overwrite);
    }
    
    @Override
    public void run(CoreSession session, DocumentModel context, boolean overwrite) throws NuxeoException {
        String contextPath = "/";
        if (context != null && !"/".equals(context.getPathAsString())) {
            contextPath = context.getPathAsString().concat("/");
        }
        for (DocumentCreationDescriptor descriptor : creationDescriptors) {
            
            // Build path
            String path = descriptor.path;
            if (path == null) {
                path = generatePathSegment(descriptor.title);
            }
            else if (path.endsWith("/")) {
                path = path.concat(generatePathSegment(descriptor.title));
            }
            if (!path.startsWith("/")) {
                path = contextPath.concat(path);
            }
            int lastSlash = path.lastIndexOf("/");
            PathRef pathRef = new PathRef(path);
            String parentPath = path.substring(0, lastSlash + 1);
            PathRef parentPathRef = new PathRef(parentPath);
            String name = path.substring(lastSlash + 1);

            // Create document or get existing document to overwrite
            boolean exists = session.exists(pathRef);
            if (!exists || overwrite) {
                boolean alreadyCreated = false;
                DocumentModel model = null;
                if (session.exists(parentPathRef)) {
                    if (exists) {
                        model = session.getDocument(pathRef);
                        alreadyCreated = true;
                    }
                    else {
                        model = session.createDocumentModel(parentPath, name, descriptor.doctype);
                    }
                }
                else {
                    logger.warn("Cannot create " + descriptor.path + " for script " + this.scriptName + ": parent document not found");
                }
                
                // Add facets
                if (descriptor.facets != null) {
                    for (String facet : descriptor.facets) {
                        model.addFacet(facet);
                    }
                }
                
                // Add local configuration
                if (descriptor.allowedTypes != null && !descriptor.allowedTypes.isEmpty()) {
                    if (model.isFolder()) {
                        model.addFacet("SuperSpace");
                        model.addFacet("UITypesLocalConfiguration");
                        model.setPropertyValue("uitypesconf:allowedTypes",
                                descriptor.allowedTypes.toArray(new String[]{}));
                    }
                    else {
                        logger.warn("Document at " + model.getPathAsString() +
                                " is not folderish, won't set local allowed types");
                    }
                }
                
                // Write properties
                if (descriptor.properties != null) {
                    for (PropertyDescriptor property : descriptor.properties) {
                        model.setPropertyValue(property.getXpath(), property.getValue());
                    }
                }
                if (descriptor.title != null) {
                    model.setPropertyValue("dc:title", descriptor.title);
                }

                // Create if needed (required to set rights)
                if (!alreadyCreated) {
                    model = session.createDocument(model);
                }
                else {
                    model = session.saveDocument(model);
                }
                
                // Set rights (except during tests, otherwise it could hide documents from the test core session)
                if (descriptor.acl != null) {
                    List<ACEDescriptor> aces = descriptor.acl.getACEs();
                    if (aces != null && !aces.isEmpty()) {
                        ACP acp = model.getACP();
                        ACL acl = acp.getOrCreateACL();
                        acl.clear();
                        if (Framework.isTestModeSet()) {
                            acl.add(new ACE("Administrator", "Everything", true));
                        }
                        for (ACEDescriptor ace : descriptor.acl.getACEs()) {
                            acl.add(new ACE(ace.getPrincipal(), ace.getPermission(), ace.isGranted()));
                        }
                        if (descriptor.acl.isBlockInheritance()) {
                            acl.add(new ACE(SecurityConstants.EVERYONE, SecurityConstants.EVERYTHING, false));
                        }
                        acp.addACL(acl);
                        model.setACP(acp, true);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return scriptName;
    }

    // Taken from PathSegmentServiceDefault
    public Pattern stupidRegexp = Pattern.compile("^[- .,;?!:/\\\\'\"]*$");
    public int maxSize = 24;
    protected String generatePathSegment(String s) throws NuxeoException {
        if (s == null) {
            s = "";
        }
        s = s.trim().toLowerCase();
        if (s.length() > maxSize) {
            s = s.substring(0, maxSize).trim();
        }
        s = s.replace('/', '-');
        s = s.replace('\\', '-');
        if (stupidRegexp.matcher(s).matches()) {
            return IdUtils.generateStringId();
        }
        return s;
    }

}
