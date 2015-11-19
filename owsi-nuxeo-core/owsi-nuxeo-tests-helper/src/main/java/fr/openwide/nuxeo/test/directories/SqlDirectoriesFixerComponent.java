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

package fr.openwide.nuxeo.test.directories;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.directory.sql.SQLDirectoryDescriptor;
import org.nuxeo.ecm.directory.sql.SQLDirectoryFactory;
import org.nuxeo.ecm.directory.sql.SQLDirectoryRegistry;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class SqlDirectoriesFixerComponent extends DefaultComponent {
    
    public static final String NAME = "fr.openwide.nuxeo.test.directories.SqlDirectoriesFixer";
    
    private final Logger logger = Logger.getLogger(SqlDirectoriesFixerComponent.class);
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        SqlDirectoryReferenceDescriptor descriptor = (SqlDirectoryReferenceDescriptor) contribution;
        String directoryName = descriptor.getName();

        // Fetch existing directory, through reflection because it's the only way
        SQLDirectoryFactory sqlDirectoryFactory = (SQLDirectoryFactory) 
            Framework.getRuntime().getComponent(SQLDirectoryFactory.NAME);
        SQLDirectoryDescriptor directoryDescriptor = null;
        try {
            directoryDescriptor = fetchDescriptorThroughReflection(sqlDirectoryFactory, directoryName);
        }
        catch (ClassNotFoundException e) {
            logger.error("Couldn't fix directory, it seems like there has been some unsupported api change.", e);
        }
        catch (NoSuchFieldException e) {
            logger.error("Couldn't fix directory, it seems like there has been some unsupported api change.", e);
        }
        catch (IllegalAccessException e) {
            logger.error("Couldn't fix directory due to invalid use of the reflect API.", e);
        }
        catch (SecurityException e) {
            logger.error("Couldn't fix directory due to invalid use of the reflect API.", e);
        }
        
        if (directoryDescriptor != null) {
            // Unregister directory
            sqlDirectoryFactory.unregisterContribution(directoryDescriptor, "directories", null);
            
            // Set up new directory, using the database embedded for tests
            directoryDescriptor.dataSourceName = null;
            directoryDescriptor.dbDriver = "${nuxeo.test.vcs.driver}";
            directoryDescriptor.dbUrl = "${nuxeo.test.vcs.url}";
            directoryDescriptor.dbUser = "${nuxeo.test.vcs.user}";
            directoryDescriptor.dbPassword = "${nuxeo.test.vcs.password}";
            sqlDirectoryFactory.registerContribution(directoryDescriptor, "directories", null);
        }
        else {
            logger.error("Descriptor for '" + directoryName + "' not found, cannot fix it");
        }
    }
   
    public SQLDirectoryDescriptor fetchDescriptorThroughReflection(SQLDirectoryFactory sqlDirectoryFactory, String directoryName)
            throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalAccessException {
        SQLDirectoryRegistry sqlDirectoryRegistry = fetchHiddenFieldThroughReflection(
                sqlDirectoryFactory, "directories", SQLDirectoryRegistry.class);
        Map<?,?> sqlDirectoryDescriptorsMap = fetchHiddenFieldThroughReflection(
                sqlDirectoryRegistry, "descriptors", Map.class);
        return (SQLDirectoryDescriptor) sqlDirectoryDescriptorsMap.get(directoryName);
    }

    @SuppressWarnings("unchecked")
    private <T> T fetchHiddenFieldThroughReflection(Object object, String fieldName, Class<T> fieldClass)
            throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> c = Class.forName(object.getClass().getName());
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        T fieldValue = (T) field.get(object);
        field.setAccessible(false);
        return fieldValue;
    }
    
}
