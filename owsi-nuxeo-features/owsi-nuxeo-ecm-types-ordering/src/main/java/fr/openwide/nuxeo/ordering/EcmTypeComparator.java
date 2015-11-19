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
package fr.openwide.nuxeo.ordering;

import java.util.Comparator;

import org.nuxeo.ecm.platform.types.Type;

/**
 * 
 * @author mkalam-alami
 *
 */
public class EcmTypeComparator implements Comparator<Type> {
    
    private final EcmTypeSortMethod sortMethod;

    public EcmTypeComparator(EcmTypeSortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }
    
    @Override
    public int compare(Type t1, Type t2) {
        if (sortMethod == EcmTypeSortMethod.ALPHABETICAL) {
            return t1.getLabel().compareTo(t2.getLabel());
        }
        else {
            // Default
            return t1.getLabel().compareTo(t2.getLabel());
        }
    }
    
}
