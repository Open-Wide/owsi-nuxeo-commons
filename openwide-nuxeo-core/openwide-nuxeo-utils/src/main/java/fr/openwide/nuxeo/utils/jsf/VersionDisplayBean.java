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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.runtime.api.Framework;

/**
 * 
 * @author mkalam-alami
 *
 */
@Name("versionDisplay")
@Scope(ScopeType.APPLICATION)
public class VersionDisplayBean implements Serializable {

    public static final String UNKNOWN_VERSION = "(unknown)";

    private static final Logger logger = Logger.getLogger(VersionDisplayBean.class);

    private static final long serialVersionUID = 1L;

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    private static String versionCache = null;

    public String getLabel() throws Exception {
        return getPrefix() + getNumber();
    }
    
    public String getPrefix() throws Exception {
        return Framework.getService(VersionDisplayService.class).getVersionPrefix();
    }
    
    public String getNumber() throws Exception {
        Pattern bundleNamePattern = Framework.getService(VersionDisplayService.class).getBundleMatchPattern();
        if (versionCache == null) {
            // Gather all bundle names
            String resourceJarPath = this.getClass().getResource("/").getPath();
            List<String> bundles = new ArrayList<String>();
            String bundlesPath = resourceJarPath.replaceAll("nxserver.*", "nxserver/bundles");
            String pluginsPath = resourceJarPath.replaceAll("nxserver.*", "nxserver/plugins");
            bundles.addAll(Arrays.asList(new File(bundlesPath).list()));
            bundles.addAll(Arrays.asList(new File(pluginsPath).list()));
            
            // Find a matching bundle
            for (String bundlePath : bundles) {
                if (bundleNamePattern.matcher(bundlePath).matches()) {
                    versionCache = extractVersion(bundlePath);
                }
            }
            if (UNKNOWN_VERSION.equals(versionCache)) {
                logger.error("Could not find version number (looked for a bundle matching '"
                        + bundleNamePattern.pattern() + "' at '" + bundlesPath + "' and '" + pluginsPath + "')");
            }
        }
        return versionCache;
    }

    public static String extractVersion(String bundlePath) {
        String[] splitName = bundlePath.split("(-|\\.[a-zA-Z].*$)");
        String versionNumber = null;
        for (String namePart : splitName) {
            if (namePart.matches("[0-9.]*")) {
                versionNumber = namePart;
            }
            if (namePart.equals("SNAPSHOT")) {
                versionNumber += "-SNAPSHOT";
            }
        }
        return (versionNumber != null) ? versionNumber : UNKNOWN_VERSION;
    }
    
}
