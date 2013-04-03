package fr.openwide.nuxeo.avatar;

import java.io.File;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.user.center.profile.UserProfileConstants;
import org.nuxeo.ecm.user.center.profile.UserProfileService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class AvatarImporterServiceImpl extends DefaultComponent implements AvatarImporterService {

    public static Logger logger = Logger.getLogger(AvatarImporterServiceImpl.class);

    public static final String EXTENSION_POINT_CONFIG = "config";
    
    private static String avatarFolderPath = null;
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (EXTENSION_POINT_CONFIG.equals(extensionPoint)) {
            registerConfiguration((AvatarImporterConfigDescriptor) contribution);
        }
    }
    
    public void registerConfiguration(AvatarImporterConfigDescriptor config) {
        avatarFolderPath = config.dossierAvatars;
    }

    public String getAvatarFolderPath() {
        return avatarFolderPath;
    }

    public void importAvatars(CoreSession coreSession) throws ClientException {
        if (avatarFolderPath != null) {
            File avatarFolder = new File(avatarFolderPath);
            if (avatarFolder.exists() && avatarFolder.isDirectory()) {
                int replacedAvatars = 0;
                try {
                    // Browse avatars
                    UserProfileService userProfileService = Framework.getService(UserProfileService.class);
                    File[] avatarFiles = avatarFolder.listFiles();
                    for (File avatarFile : avatarFiles) {
                        
                        // Look for the related user
                        String filename = avatarFile.getName();
                        String username = filename.substring(0, filename.lastIndexOf("."));
                        DocumentModel userProfile = userProfileService.getUserProfileDocument(username, coreSession);
                        if (userProfile != null) {
                            boolean replaceBlob = true;
                            
                            // Compare pictures
                            Blob previousBlob = (Blob) userProfile.getPropertyValue(UserProfileConstants.USER_PROFILE_AVATAR_FIELD);
                            FileBlob newBlob = new FileBlob(avatarFile);
                            if (previousBlob != null) {
                                long previousBlobLength = previousBlob.getLength(); // XXX can't compare hashes, getDigest() is only implemented on SQLBlob 
                                if (previousBlobLength == newBlob.getLength()) {
                                    replaceBlob = false;
                                }
                            }
                            
                            // (Over)write picture
                            if (replaceBlob) {
                                userProfile.setPropertyValue(UserProfileConstants.USER_PROFILE_AVATAR_FIELD, newBlob);
                                coreSession.saveDocument(userProfile);
                                replacedAvatars++;
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new ClientException(e);
                }
                if (replacedAvatars > 0) {
                    logger.info(replacedAvatars + " avatars imported from " + avatarFolderPath);
                }
            }
            else {
                logger.warn("Cannot import avatars, folder not found : " + avatarFolderPath);
            }
        }
    }
    
}
