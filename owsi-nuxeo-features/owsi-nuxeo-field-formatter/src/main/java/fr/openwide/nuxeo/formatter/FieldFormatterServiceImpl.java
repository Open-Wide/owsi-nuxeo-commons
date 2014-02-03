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
package fr.openwide.nuxeo.formatter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * 
 * @author mkalam-alami
 * 
 */
public class FieldFormatterServiceImpl extends DefaultComponent implements FieldFormatterService {

    public static final String EXTENSION_POINT_PATTERNS = "patterns";

    private static final Logger logger = Logger.getLogger(FieldFormatterServiceImpl.class);
    
    private Map<String, String> patterns = new HashMap<String, String>();

    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (EXTENSION_POINT_PATTERNS.equals(extensionPoint)) {
            PatternDescriptor patternDescriptor = (PatternDescriptor) contribution;
            registerPattern(patternDescriptor.getName(), patternDescriptor.getPattern());
        }
    }

    @Override
    public String getPattern(String name) {
        return patterns.get(name);
    }

    @Override
    public void registerPattern(String name, String pattern) {
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(pattern)) {
            patterns.put(name, pattern);
        }
        else {
            logger.error("Invalid pattern contribution: name=" + name + ", pattern=" + pattern);
        }
    }

    @Override
    public Set<String> getPatternNames() {
        return Collections.unmodifiableSet(patterns.keySet());
    };
}
