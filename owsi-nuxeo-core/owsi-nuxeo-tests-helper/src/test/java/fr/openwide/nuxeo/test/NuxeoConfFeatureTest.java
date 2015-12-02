package fr.openwide.nuxeo.test;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.runtime.api.Framework;

public class NuxeoConfFeatureTest extends AbstractNuxeoTest {

    private static final String PROPERTY_NAME_TEST = "test.property";

    @Test
    public void testPropertyLoader() throws DirectoryException {
        Assert.assertTrue("nuxeo.conf file incorrectly loaded", Framework.isBooleanPropertyTrue(PROPERTY_NAME_TEST));
    }

}
