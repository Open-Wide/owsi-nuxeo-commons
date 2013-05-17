package fr.openwide.nuxeo.utils;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.test.AbstractNuxeoTest;
import fr.openwide.nuxeo.utils.jsf.VersionDisplayBean;
import fr.openwide.nuxeo.utils.jsf.VersionDisplayService;

@Deploy("fr.openwide.nuxeo.commons.utils")
@LocalDeploy("fr.openwide.nuxeo.commons.utils:OSGI-INF/versiondisplay-test.xml")
public class VersionDisplayServiceTest extends AbstractNuxeoTest  {

    @Inject
    VersionDisplayService versionDisplayService;
    
    @Test
    public void testExtensionPoint() throws ClientException {
        Assert.assertEquals("hello", versionDisplayService.getVersionPrefix());
        Assert.assertEquals("world", versionDisplayService.getBundleMatchPattern().toString());
    }
    
    @Test
    public void testVersionParsing() throws ClientException {
        Assert.assertEquals("1.0", VersionDisplayBean.extractVersion("hello-1.0.jar"));
        Assert.assertEquals("1.1", VersionDisplayBean.extractVersion("hello-1-world-1.1.jar"));
        Assert.assertEquals("1.2-SNAPSHOT", VersionDisplayBean.extractVersion("hello-world-1.2-SNAPSHOT.jar"));
        Assert.assertEquals("1.3-SNAPSHOT", VersionDisplayBean.extractVersion("hello-1.5-world-1.3-SNAPSHOT.zip"));
    }
}
