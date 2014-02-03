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
package fr.openwide.nuxeo.propertysync.listener;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.api.model.PropertyException;

import fr.openwide.nuxeo.propertysync.PropertySyncException;
import fr.openwide.nuxeo.propertysync.service.PropertySyncService;

/**
 * @author mkalam-alami
 * @author schambon
 * 
 */
public abstract class AbstractPropertySyncRunner extends UnrestrictedSessionRunner {

    @SuppressWarnings("unused")
    private final Logger logger = Logger.getLogger(AbstractPropertySyncRunner.class);

    protected final DocumentModel doc;

    protected AbstractPropertySyncRunner(CoreSession session, DocumentModel doc) {
        super(session);
        this.doc = doc;
    }

    /**
     * 
     * @param fromModel
     * @param fromXPath
     * @param toModel
     * @param toXPath
     * @param onlyIfNull if true, only copies the property if toModel.getPropertyValue(toXPath) is null.
     * @return true if an actual copy occurred
     * @throws ClientException
     */
    protected boolean copyPropertyValue(DocumentModel fromModel, String fromXPath, DocumentModel toModel, String toXPath,
            boolean onlyIfNull) throws ClientException {
        Property prop = null;
        try {
            prop = toModel.getProperty(toXPath);
        } catch (PropertyException e) {
            throw new PropertySyncException("Property " + toXPath + " was not found on source " + toModel.toString(), e);
        }

        Object value = null;
        if (PropertySyncService.XPATH_UUID.equals(fromXPath)) {
            value = fromModel.getId();
        } else if (PropertySyncService.XPATH_NAME.equals(fromXPath)) {
            value = fromModel.getName();
        } else {
            Property prop2 = null;
            try {
                prop2 = fromModel.getProperty(fromXPath);
            } catch (PropertyException e) {
                throw new PropertySyncException("Property " + toXPath + " was not found on target "
                        + toModel.toString(), e);
            }
            value = prop2.getValue();
        }
        if (value != null) {
            // Override
            Serializable oldValue = prop.getValue();
            if (!onlyIfNull || oldValue == null) {
                prop.setValue(value);
                return true;
            }

            // Merge (TODO Allow to choose merge policy)
            /* if (prop.isScalar()) {
                     prop.setValue(value);
                 } else {
                     Object[] values = (Object[]) prop.getValue();
                     if (values != null) {
                         if (!value.getClass().isArray()) {
                             Object[] values2 = Arrays.copyOf(values, values.length + 1);
            //                                            Object[] values2 = new Object[values.length + 1];
            //                                            System.arraycopy(values, 0, values2, 0, values.length);
                             values2[values.length] = value;
                             prop.setValue(values2);
                         } else {
                             Object[] arrayValue = (Object[]) value;
                             Object[] values2 = Arrays.copyOf(values, values.length + arrayValue.length);
                             System.arraycopy(arrayValue, values.length, values2, values.length, arrayValue.length);
                             prop.setValue(values2);
                         }
                     } else prop.setValue(new Object[]{value});
                 }*/
        }
        
        return false;
    }

}
