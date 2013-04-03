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
package fr.openwide.nuxeo.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * 
 * @author mkalam-alami
 *
 */
public class PropertiesServiceImpl extends DefaultComponent implements PropertiesService {

    private static Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
    
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor) throws Exception {
        PropertyDescriptor descriptor = (PropertyDescriptor) contribution;
        properties.put(descriptor.getName(), descriptor);
    }
    
    @Override
    public String getValue(String key) {
        return getValue(key, null);
    }

    @Override
    public String getValue(String key, String defaultValue) {
        PropertyDescriptor property = properties.get(key);
        return (property != null) ? property.getStringValue() : defaultValue;
    }

    @Override
    public Long getNumberValue(String key) {
        return getNumberValue(key, null);
    }

    @Override
    public Long getNumberValue(String key, Long defaultValue) {
        PropertyDescriptor property = properties.get(key);
        return (property != null) ? property.getNumberValue() : defaultValue;
    }

    @Override
    public List<String> getListValue(String key) {
        return getListValue(key, null);
    }

    @Override
    public List<String> getListValue(String key, List<String> defaultValue) {
        PropertyDescriptor property = properties.get(key);
        return (property != null) ? property.getListValue() : defaultValue;
    }

    @Override
    public Map<String, String> getMapValue(String key) {
        return getMapValue(key, null);
    }

    @Override
    public Map<String, String> getMapValue(String key, Map<String, String> defaultValue) {
        PropertyDescriptor property = properties.get(key);
        return (property != null) ? property.getMapValue() : defaultValue;
    }
    
}
