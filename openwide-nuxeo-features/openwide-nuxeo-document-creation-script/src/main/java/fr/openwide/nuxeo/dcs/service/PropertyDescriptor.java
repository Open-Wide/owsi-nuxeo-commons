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
package fr.openwide.nuxeo.dcs.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 
 * @author mkalam-alami
 *
 */
@XObject(value = "property")
public class PropertyDescriptor {

    @XNode("@xpath")
    private String xpath;

    @XNode("@value")
    private String value;

    public PropertyDescriptor() {
        
    }
    
    public PropertyDescriptor(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }
    
    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return value;
    }

}
