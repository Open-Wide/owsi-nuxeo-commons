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
package fr.openwide.nuxeo.utils.automation;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.nuxeo.ecm.automation.ExitException;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.jsf.OperationHelper;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;

public class OperationUtils {

    public static DocumentModelList getInputAsModelList(OperationContext ctx) {
        Object input = ctx.getInput();
        DocumentModelList models = new DocumentModelListImpl();
        if (input instanceof DocumentModel) {
            models.add((DocumentModel) input);
        } else if (input instanceof DocumentModelList) {
            models = (DocumentModelList) input;
        }
        return models;
    }

    public static List<String> getInputAsModelIds(OperationContext ctx) {
        DocumentModelList inputAsModelList = getInputAsModelList(ctx);
        List<String> inputAsIds = new ArrayList<String>();
        if (inputAsModelList != null) {
            for (DocumentModel model : inputAsModelList) {
                inputAsIds.add(model.getId());
            }
        }
        return inputAsIds;
    }

    public static void addFacesMessage(Severity severity, String message) throws ExitException {
        FacesMessages.instance().add(severity, message);
    }
    
    public static void fail() throws ExitException {
        OperationHelper.getWebActions().resetCurrentTabs(); // needed to avoid having the next click on a link do nothing
        throw new ExitException(null, true);
    }

}
