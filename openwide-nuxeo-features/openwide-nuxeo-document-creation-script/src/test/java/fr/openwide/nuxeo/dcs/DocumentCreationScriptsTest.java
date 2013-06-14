package fr.openwide.nuxeo.dcs;

import junit.framework.Assert;

import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.dcs.service.DocumentCreationDescriptor;
import fr.openwide.nuxeo.dcs.service.DocumentCreationScriptService;
import fr.openwide.nuxeo.test.AbstractNuxeoTest;
import fr.openwide.nuxeo.types.TypeDomain;
import fr.openwide.nuxeo.types.TypeFile;
import fr.openwide.nuxeo.types.TypeFolder;

/**
 * 
 * @author mkalam-alami
 * 
 */
@Deploy({
    "org.nuxeo.ecm.platform.types.core", // Enable local configuration
    "fr.openwide.nuxeo.commons.dcs"
})
@LocalDeploy("fr.openwide.nuxeo.commons.dcs:OSGI-INF/document-creation-script-test.xml")
public class DocumentCreationScriptsTest extends AbstractNuxeoTest {

    private static final String SCRIPT_NAME = "myscript";
    
    @Inject
    DocumentCreationScriptService dcs;

    @Inject
    CoreSession documentManager;

    @Test
    public void inlineScript() throws Exception {
        DocumentCreationScript script = new DocumentCreationScriptImpl(SCRIPT_NAME);
        
        // Register script
        script.appendDocumentCreation(new DocumentCreationDescriptor("/test", TypeDomain.TYPE)
                .addFacet("HiddenInNavigation")
                .addProperty(TypeDomain.XPATH_TITLE, "Hello")
                .addACE("Members", "Read", false)
                .addAllowedType(TypeFile.TYPE));
        dcs.registerScript(script);
        
        // Execute
        dcs.runScript(documentManager, SCRIPT_NAME, false);
        
        // Check results
        PathRef createdDomain = new PathRef("/test");
        Assert.assertTrue("The document must be created", documentManager.exists(createdDomain));
        DocumentModel domain = documentManager.getDocument(createdDomain);
        
        Assert.assertTrue("Facets must be set", domain.hasFacet("HiddenInNavigation"));
        
        Assert.assertEquals("Hello", domain.getTitle());
        
        ACL localAcl = domain.getACP().getACL(ACL.LOCAL_ACL);
        Assert.assertEquals("Local ACL must be set (1/4)", 2, localAcl.getACEs().length); // In tests, 1 additional ACE is set
        ACE ace = localAcl.getACEs()[1];
        Assert.assertEquals("Local ACL must be set (2/4)", "Members", ace.getUsername());
        Assert.assertEquals("Local ACL must be set (3/4)", "Read", ace.getPermission());
        Assert.assertEquals("Local ACL must be set (4/4)", false, ace.isGranted());
        
        Assert.assertTrue("Local config must be enabled", domain.hasFacet("UITypesLocalConfiguration"));
        String[] allowedTypes = (String[]) domain.getPropertyValue("uitypesconf:allowedTypes");
        Assert.assertTrue("Local types must be set", allowedTypes.length == 1 && "File".equals(allowedTypes[0]));
    }

    @Test
    public void xmlScript() throws Exception {
        DocumentModel folder = documentManager.createDocumentModel("/", "folder", TypeFolder.TYPE);
        folder = documentManager.createDocument(folder);
        
        dcs.runScript(documentManager, "test", folder, false); // Run locally at /folder
        
        PathRef docRef = new PathRef("/folder/fichierLocal");
        Assert.assertTrue("The documents must be created", documentManager.exists(docRef));
        DocumentModel document = documentManager.getDocument(docRef);
        Assert.assertEquals("Foo", document.getTitle());
        Assert.assertEquals("Bar", document.getPropertyValue("dc:description"));

        PathRef folderRef = new PathRef("/folder/myfolder");
        Assert.assertTrue("The documents must be created", documentManager.exists(folderRef));
        Assert.assertEquals(1, documentManager.getChildren(folderRef).size());
    }

}