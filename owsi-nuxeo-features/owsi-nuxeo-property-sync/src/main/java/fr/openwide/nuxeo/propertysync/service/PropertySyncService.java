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

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @author schambon
 * @author mkalam-alami
 */
public interface PropertySyncService {

    static final String CONTEXT_TRIGGERED_BY_PROPERTY_SYNC = "propertySync";
    
    static final String CONTEXT_BYPASS_PROPERTY_SYNC = "bypassPropertySync";

    static final String XPATH_NAME = "ecm:name";

    static final String XPATH_UUID = "ecm:uuid";
    
    List<RuleDescriptor> getDescriptors(DocumentModel targetModel);

    Map<String, RuleDescriptor> getTargetDoctypes(String sourceType);

    Map<String, RuleDescriptor> getTargetFacets(String sourceType);
    
    void registerRule(RuleDescriptor descriptor);
    
}
