/*******************************************************************************
 * (C) Copyright 2014 Open Wide (http://www.openwide.fr/) and others.
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
package fr.openwide.nuxeo.utils.user;

import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * 
 * @author mdutoo
 * 
 */
public class UserUtils {

   public static String computeFullName(NuxeoPrincipal principal) {
       // copied from DefaultUserWorkspaceServiceImpl.buildUserWorkspaceTitle(String)
       StringBuilder title = new StringBuilder();
       String firstName = principal.getFirstName();
       if (firstName != null && firstName.trim().length() > 0) {
           title.append(firstName);
       }

       String lastName = principal.getLastName();
       if (lastName != null && lastName.trim().length() > 0) {
           if (title.length() > 0) {
               title.append(" ");
           }
           title.append(lastName);
       }
       return title.toString();
   }
   
}
