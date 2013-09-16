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

import java.util.List;
import java.util.Map;

/**
 * Basic properties service, for information that doesn't deserve its own extension point.
 * 
 * @author mkalam-alami
 *
 */
public interface PropertiesService {

    String getValue(String key);

    String getValue(String key, String defaultValue);

    Long getNumberValue(String key);

    Long getNumberValue(String key, Long defaultValue);
    
    List<String> getListValue(String key);

    List<String> getListValue(String key, List<String> defaultValue);

    Map<String, String> getMapValue(String key);
    
    Map<String, String> getMapValue(String key, Map<String, String> defaultValue);
}
