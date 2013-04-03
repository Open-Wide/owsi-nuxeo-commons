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
package fr.openwide.nuxeo.avatar;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;
import org.nuxeo.ecm.user.center.profile.UserProfileConstants;
import org.nuxeo.ecm.user.center.profile.UserProfileService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.test.AbstractNuxeoTest;

/**
 * 
    @Inject
    private CoreSession documentManager;
 * @author mkalam-alami
 * 
 */
@Deploy({
    "org.nuxeo.ecm.user.center.profile", // provides UserProfileService
    "fr.openwide.nuxeo.commons.avatarimporter"
})
@Features(UserWorkspaceCoreFeature.class) // Required by UserProfileService 
@LocalDeploy("fr.openwide.nuxeo.commons.avatarimporter:OSGI-INF/avatar-importer-test.xml")
public class AvatarImportTest extends AbstractNuxeoTest {

    private static final String SCHEDULER_EVENT_NAME = "avatarImport";
    
    private static final String ADMINISTRATOR_USER = "Administrator";
    
    @Inject
    private EventService eventService;
    
    @Inject
    private CoreSession documentManager;
    
    @Inject
    private UserProfileService userProfileService;
    
    @Test
    public void testImport() throws Exception {
        // Force event trigger
        eventService.fireEvent(SCHEDULER_EVENT_NAME, 
                new EventContextImpl(documentManager, documentManager.getPrincipal()));
        Framework.getService(AvatarImporterService.class).importAvatars(documentManager);
        
        // The avatars must be imported
        DocumentModel userProfileDocument = userProfileService.getUserProfileDocument(ADMINISTRATOR_USER, documentManager);
        Assert.assertNotNull("Avatars must be imported", 
                userProfileDocument.getPropertyValue(UserProfileConstants.USER_PROFILE_AVATAR_FIELD));
    }

}