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
package fr.openwide.nuxeo.propertysync.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * @author schambon
 * @author mkalam-alami
 */
public class PropertySyncServiceImpl extends DefaultComponent implements PropertySyncService {

    public static final String EXTENSION_POINT_RULE = "rule";
    
    private final Logger logger = Logger.getLogger(PropertySyncServiceImpl.class);
    
    /**
     * Sorted by doctypes/facets on which metadata will be copied
     */
    private Map<String, List<RuleDescriptor>> descriptorsByTargetType = new HashMap<String, List<RuleDescriptor>>();
    private Map<String, List<RuleDescriptor>> descriptorsByTargetFacet = new HashMap<String, List<RuleDescriptor>>();

    /**
     * Sorted by doctypes that hold metadata to copy on other doctypes/facets
     */
    private Map<String, Map<String, RuleDescriptor>> descriptorsBySourceType = new HashMap<String, Map<String, RuleDescriptor>>();
    private Map<String, Map<String, RuleDescriptor>> descriptorsBySourceFacet = new HashMap<String, Map<String, RuleDescriptor>>();

    private TypeManager typeManagerCache;


    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (EXTENSION_POINT_RULE.equals(extensionPoint)) {
            registerRule((RuleDescriptor) contribution);
        }
    }

    @Override
    public void registerRule(RuleDescriptor descriptor) {
        // Register by doctype
        for (String doctype : descriptor.getDoctypes()) {
            if (!descriptorsByTargetType.containsKey(doctype)) {
                descriptorsByTargetType.put(doctype, new ArrayList<RuleDescriptor>());
            }
            descriptorsByTargetType.get(doctype).add(descriptor);

            // Register inverse references
            for (PropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
                String sourceType = propertyDescriptor.getAncestorType();
                if (!descriptorsBySourceType.containsKey(sourceType)) {
                    descriptorsBySourceType.put(sourceType, new HashMap<String, RuleDescriptor>());
                }
                descriptorsBySourceType.get(sourceType).put(doctype, descriptor);
            }
        }

        // Register by facet
        for (String facet : descriptor.getFacets()) {
            if (!descriptorsByTargetFacet.containsKey(facet)) {
                descriptorsByTargetFacet.put(facet, new ArrayList<RuleDescriptor>());
            }
            descriptorsByTargetFacet.get(facet).add(descriptor);

            // Register inverse references
            for (PropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
                String sourceType = propertyDescriptor.getAncestorType();
                if (!descriptorsBySourceFacet.containsKey(sourceType)) {
                    descriptorsBySourceFacet.put(sourceType, new HashMap<String, RuleDescriptor>());
                }
                descriptorsBySourceFacet.get(sourceType).put(facet, descriptor);
            }
        }
    }

    /**
     * @return the typeManager
     */
    public TypeManager getTypeManager() {
        if (typeManagerCache == null) {
            try {
                typeManagerCache = Framework.getService(TypeManager.class);
            }
            catch (Exception e) {
                logger.error("Failed to get TypeManager, PropertySyncService won't work", e);
            }
        }
        return typeManagerCache;
    }

    @Override
    public List<RuleDescriptor> getDescriptors(DocumentModel model) {
        List<RuleDescriptor> descriptors = new ArrayList<RuleDescriptor>();
        
        String modelDoctype = model.getType();
        List<String> typeHierarchy = getTypeHierarchy(modelDoctype);
        for (String doctype : typeHierarchy) {
            List<RuleDescriptor> byType = descriptorsByTargetType.get(doctype);
            if (byType != null) {
                descriptors.addAll(byType);
            }
        }
        
        for (Entry<String, List<RuleDescriptor>> descriptorsByFacet : descriptorsByTargetFacet.entrySet()) {
            if (model.hasFacet(descriptorsByFacet.getKey())) {
                for (RuleDescriptor descriptorByFacet : descriptorsByFacet.getValue()) {
                    if (!descriptors.contains(descriptorByFacet)) {
                        descriptors.add(descriptorByFacet);
                    }
                }
            }
        }
        return descriptors;
    }

    @Override
    public Map<String, RuleDescriptor> getTargetDoctypes(String sourceType) {
        Map<String, RuleDescriptor> descriptorMap = new HashMap<String, RuleDescriptor>();
        List<String> typeHierarchy = getTypeHierarchy(sourceType);
        for (String doctype : typeHierarchy) {
            if (descriptorsBySourceType.containsKey(doctype)) {
                descriptorMap.putAll(descriptorsBySourceType.get(doctype));
            }
        }
        return descriptorMap;
    }
    
    @Override
    public Map<String, RuleDescriptor> getTargetFacets(String sourceType) {
        Map<String, RuleDescriptor> descriptorMap = new HashMap<String, RuleDescriptor>();
        List<String> typeHierarchy = getTypeHierarchy(sourceType);
        for (String doctype : typeHierarchy) {
            if (descriptorsBySourceFacet.containsKey(doctype)) {
                descriptorMap.putAll(descriptorsBySourceFacet.get(doctype));
            }
        }
        return descriptorMap;
    }

    private List<String> getTypeHierarchy(String modelDoctype) {
        List<String> typeHierarchy = new ArrayList<String>();
        typeHierarchy.addAll(Arrays.asList(getTypeManager().getSuperTypes(modelDoctype)));
        typeHierarchy.add(modelDoctype);
        return typeHierarchy;
    }

}
