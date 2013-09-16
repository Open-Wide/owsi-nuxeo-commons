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
package fr.openwide.nuxeo.ordering.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("category")
public class OrderingCategoryDescriptor {

    @XNode("@name")
    protected String name;

    @XNode("@default")
    protected boolean isDefault = false;
    
    @XNodeList(value = "type", type = String[].class, componentType = String.class)
    protected String[] types;

    public String getName() {
        return name;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public String[] getTypes() {
        return types;
    }
    
}
