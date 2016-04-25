package fr.openwide.nuxeo.avatar;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.blob.binary.DefaultBinaryManager;
import org.nuxeo.ecm.user.center.profile.UserProfileConstants;
import org.nuxeo.ecm.user.center.profile.UserProfileService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class AvatarImporterServiceImpl extends DefaultComponent implements AvatarImporterService {

    public static Logger logger = Logger.getLogger(AvatarImporterServiceImpl.class);

    public static final String EXTENSION_POINT_CONFIG = "config";
    
    private static String avatarFolderPath = null;

    private static MessageDigest messageDigest = null;
    
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 digest missing from this Java platform", e);
        }
    }
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (EXTENSION_POINT_CONFIG.equals(extensionPoint)) {
            registerConfiguration((AvatarImporterConfigDescriptor) contribution);
        }
    }
    
    @Override
    public void registerConfiguration(AvatarImporterConfigDescriptor config) {
        avatarFolderPath = config.getAvatarFolder();
    }

    @Override
    public String getAvatarFolderPath() {
        return avatarFolderPath;
    }

    @Override
    public void importAvatars(CoreSession coreSession) throws NuxeoException {
        if (avatarFolderPath != null) {
            File avatarFolder = new File(avatarFolderPath);
            if (avatarFolder.exists() && avatarFolder.isDirectory()) {
                int replacedAvatars = 0;
                try {
                    // Browse avatars
                    UserProfileService userProfileService = Framework.getService(UserProfileService.class);
                    File[] avatarFiles = avatarFolder.listFiles();
                    for (File avatarFile : avatarFiles) {
                        if (avatarFile.isFile()) {
                        
                            // Look for the related user
                            String filename = avatarFile.getName();
                            String username = filename.substring(0, filename.lastIndexOf("."));
                            DocumentModel userProfile = null;
                            try {
                                userProfile = userProfileService.getUserProfileDocument(username, coreSession);
                            }
                            catch (NuxeoException e) {
                                logger.error("Could not get user profile for '" + username 
                                        + "', skipping it (file: " + avatarFile.getAbsolutePath()
                                        + ", cause: " + e.getMessage() + ")");
                            }
                            if (userProfile != null) {
                                boolean replaceBlob = true;
                                
                                // Compare pictures
                                Blob previousBlob = (Blob) userProfile.getPropertyValue(UserProfileConstants.USER_PROFILE_AVATAR_FIELD);
                                FileBlob newBlob = new FileBlob(avatarFile);
                                if (previousBlob != null) {
                                    if (previousBlob.getDigest().equals(
                                            DefaultBinaryManager.toHexString(messageDigest.digest(newBlob.getByteArray()))
                                                )) {
                                        replaceBlob = false;
                                    }
                                }
                                
                                // (Over)write picture
                                if (replaceBlob) {
                                    userProfile.setPropertyValue(UserProfileConstants.USER_PROFILE_AVATAR_FIELD, newBlob);
                                    coreSession.saveDocument(userProfile);
                                    try {
                                        coreSession.saveDocument(userProfile);
                                    } catch (Exception e) {
                                        throw new NuxeoException("Error while saving user profile for " + username + " (avatar path: " + avatarFile.getAbsolutePath() + ")", e);
                                    }
                                    replacedAvatars++;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new NuxeoException(e);
                }
                if (replacedAvatars > 0) {
                    logger.info(replacedAvatars + " avatars imported from " + avatarFolderPath);
                }
            }
            else {
                logger.warn("Cannot import avatars, folder not found: " + avatarFolderPath);
            }
        }
    }
    
}
