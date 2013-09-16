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
package fr.openwide.nuxeo.ordering.web;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService;

/**
 * 
 * @author mkalam-alami
 *
 */
@Name("ecmTypesOrdering")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK)
public class EcmTypesOrderingServiceWrapperBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private EcmTypesOrderingService ecmTypesOrderingService;

    @Unwrap
    public EcmTypesOrderingService getEcmTypesOrderingService() throws Exception {
        if (ecmTypesOrderingService == null) {
            ecmTypesOrderingService = Framework.getService(EcmTypesOrderingService.class);
        }
        return ecmTypesOrderingService;
    }

}
