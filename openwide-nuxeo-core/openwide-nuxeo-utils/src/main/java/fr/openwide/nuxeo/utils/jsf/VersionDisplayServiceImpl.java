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

package fr.openwide.nuxeo.utils.jsf;

import java.util.regex.Pattern;

import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;


public class VersionDisplayServiceImpl extends DefaultComponent implements VersionDisplayService {
   
    private static final String EXTENSION_POINT_CONFIG = "config";

    private String versionPrefix = null;
    
    private Pattern bundleMatchPattern = null;
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (EXTENSION_POINT_CONFIG.equals(extensionPoint)) {
            VersionDisplayConfigDescriptor descriptor = (VersionDisplayConfigDescriptor) contribution;
            versionPrefix = descriptor.getVersionNumberPrefix();
            bundleMatchPattern = descriptor.getBundleMatchPattern();
        }
    }
    
    public String getVersionPrefix() {
        return versionPrefix;
    }
    
    public void setVersionPrefix(String versionPrefix) {
        this.versionPrefix = versionPrefix;
    }
    
    public Pattern getBundleMatchPattern() {
        return bundleMatchPattern;
    }
    
    public void setBundleMatchPattern(Pattern bundleMatchPattern) {
        this.bundleMatchPattern = bundleMatchPattern;
    }
    
}
