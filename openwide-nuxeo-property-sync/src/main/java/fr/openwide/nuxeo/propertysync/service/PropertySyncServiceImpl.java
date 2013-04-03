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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * @author schambon
 * @author mkalam-alami
 */
public class PropertySyncServiceImpl extends DefaultComponent implements PropertySyncService {

    public static final String EXTENSION_POINT_RULE = "rule";
    
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
            for (SetPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
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
            for (SetPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
                String sourceType = propertyDescriptor.getAncestorType();
                if (!descriptorsBySourceFacet.containsKey(sourceType)) {
                    descriptorsBySourceFacet.put(sourceType, new HashMap<String, RuleDescriptor>());
                }
                descriptorsBySourceFacet.get(sourceType).put(facet, descriptor);
            }
        }
    }

    @Override
    public List<RuleDescriptor> getDescriptors(DocumentModel targetModel) {
        List<RuleDescriptor> descriptors = new ArrayList<RuleDescriptor>();
        List<RuleDescriptor> byType = descriptorsByTargetType.get(targetModel.getType());
        if (byType != null) {
            descriptors.addAll(byType);
        }
        for (Entry<String, List<RuleDescriptor>> descriptorsByFacet : descriptorsByTargetFacet.entrySet()) {
            if (targetModel.hasFacet(descriptorsByFacet.getKey())) {
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
        return descriptorsBySourceType.get(sourceType);
    }
    
    @Override
    public Map<String, RuleDescriptor> getTargetFacets(String sourceType) {
        return descriptorsBySourceFacet.get(sourceType);
    }

}
