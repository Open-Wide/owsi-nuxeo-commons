package fr.openwide.nuxeo.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.openwide.nuxeo.test.AbstractNuxeoTest;
import fr.openwide.nuxeo.types.TypeFile;
import fr.openwide.nuxeo.utils.document.DocumentUtils;

public class DocumentUtilsTest extends AbstractNuxeoTest  {

    @Test
    public void testDiff() throws ClientException {
        DocumentModel testFile = documentManager.createDocumentModel("/", "test", TypeFile.TYPE);
        testFile.setPropertyValue(TypeFile.XPATH_TITLE, "Hello");
        testFile = documentManager.createDocument(testFile);
        
        DocumentModel testFile2 = documentManager.getDocument(testFile.getRef());
        testFile2.setPropertyValue(TypeFile.XPATH_TITLE, "World");
        testFile2.setPropertyValue(TypeFile.XPATH_DESCRIPTION, "!");
        
        List<String> differingProperties = DocumentUtils.getDifferingProperties(testFile, testFile2);
        
        Assert.assertTrue(differingProperties.contains(TypeFile.XPATH_TITLE));
        Assert.assertTrue(differingProperties.contains(TypeFile.XPATH_DESCRIPTION));
    }
}
