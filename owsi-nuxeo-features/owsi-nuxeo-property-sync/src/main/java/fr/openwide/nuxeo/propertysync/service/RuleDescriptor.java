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
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * @author schambon
 * @author mkalam-alami
 */
@XObject(value="rule")
public class RuleDescriptor {

    @XNode("@name")
    public String name;

    @XNode("@noMassUpdate")
    public boolean noMassUpdate = false;
    
    private String[] types;
    
    private String[] facets;
    
    private PropertyDescriptor[] propertyDescriptors;

    @XNodeList(
            value = "properties/property",
            componentType = PropertyDescriptor.class,
            type = PropertyDescriptor[].class
    )
    public void setProperties(PropertyDescriptor[] setProperties) {
        this.propertyDescriptors = setProperties;
    }
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }

    @XNodeList(
            value = "target/type",
            componentType = String.class,
            type = String[].class
    )
    public void setDoctypes(String[] types) {
        this.types = types;
    }
    
    public String[] getDoctypes() {
        return types;
    }
    
    @XNodeList(
            value = "target/facet",
            componentType = String.class,
            type = String[].class
    )
    public void setFacets(String[] facets) {
        this.facets = facets;
    }
    
    public String[] getFacets() {
        return facets;
    }
    
    public boolean getNoMassUpdate() {
        return noMassUpdate;
    }

    public void setNoMassUpdate(boolean noMassUpdate) {
        this.noMassUpdate = noMassUpdate;
    }
    
    @Override
    public String toString() {
        return "RuleDescriptor " + name + " (Types=" + types + ", Facets=" + facets + ")";
    }

}
