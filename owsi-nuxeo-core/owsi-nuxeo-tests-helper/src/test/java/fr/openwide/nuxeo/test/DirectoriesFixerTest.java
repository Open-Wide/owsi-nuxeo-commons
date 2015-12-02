package fr.openwide.nuxeo.test;

import org.junit.Test;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.test.AbstractNuxeoTest;

@Deploy({ "org.nuxeo.ecm.default.config", "fr.openwide.nuxeo.commons.testshelper" })
@LocalDeploy("fr.openwide.nuxeo.commons.testshelper:OSGI-INF/directory-fixer-contrib.xml")
public class DirectoriesFixerTest extends AbstractNuxeoTest {

    @Inject
    DirectoryService directoryService;

    @Test
    public void testCountryAccess() throws DirectoryException {
        directoryService.open("country").close(); // Should not fail
    }

}
