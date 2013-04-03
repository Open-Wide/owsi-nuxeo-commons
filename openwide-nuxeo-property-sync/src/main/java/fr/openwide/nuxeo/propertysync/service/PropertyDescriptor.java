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

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * @author schambon
 * @author mkalam-alami
 */
@XObject("setProperty")
public class PropertyDescriptor {

    /**
     * Required.
     * The property to update.
     */
    @XNode("@xpath")
    private String xpath;

    /**
     * Required.
     * The document type to look for, among the parents hierarchy.
     */
    @XNode("@ancestorType")
    private String ancestorType;

    /**
     * The property of the ancestor document that has to be copied.
     * If null, the "xpath" property is used.
     */
    @XNode("@ancestorXpath")
    private String ancestorXpath = null;
    
    /**
     * When enabled, the property is copied only if the
     * property of the document to override is still empty.
     */
    @XNode("@onlyIfNull")
    private boolean onlyIfNull = false;

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getAncestorType() {
        return ancestorType;
    }

    public void setAncestorType(String ancestorType) {
        this.ancestorType = ancestorType;
    }

    public String getAncestorXpath() {
        if (ancestorXpath != null) {
            return ancestorXpath;
        }
        else {
            return xpath;
        }
    }

    public void setAncestorXpath(String ancestorXpath) {
        this.ancestorXpath = ancestorXpath;
    }

    public boolean getOnlyIfNull() {
        return onlyIfNull;
    }

    public void setOnlyIfNull(boolean onlyIfNull) {
        this.onlyIfNull = onlyIfNull;
    }
    
}
