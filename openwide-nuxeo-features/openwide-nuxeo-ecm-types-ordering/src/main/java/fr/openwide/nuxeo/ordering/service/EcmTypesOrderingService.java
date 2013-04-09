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

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.types.Type;


/**
 * @author mkalam-alami
 *
 */
public interface EcmTypesOrderingService {

    Map<String, List<Type>> orderTypes(Map<String, List<Type>> types, ActionContext context);

    int getColumnSize();

}
