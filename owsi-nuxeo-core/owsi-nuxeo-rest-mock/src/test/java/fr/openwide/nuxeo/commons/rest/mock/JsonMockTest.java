package fr.openwide.nuxeo.commons.rest.mock;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.runtime.test.runner.Deploy;

import fr.openwide.nuxeo.test.AbstractWebEngineTest;


/**
 * Tests interoperability with M3P model using a mock M3P server
 * @author mdutoo
 */
@Deploy("fr.openwide.nuxeo.commons.rest.mock")
public class JsonMockTest extends AbstractWebEngineTest {

   /**
    * Checks that mock M3P server has been started
    * @throws HttpException
    * @throws IOException
    */
   @Test
   public void testMock() throws HttpException, IOException {
       HttpClient httpClient = new HttpClient();
       GetMethod get = new GetMethod(getURL(JsonMockServer.class) + "/myapi/myresponse");
       int statusCode = httpClient.executeMethod(get);
       Assert.assertEquals(200, statusCode);
       String response = get.getResponseBodyAsString();
       Assert.assertTrue(response.contains("cell"));
   }
   
}
