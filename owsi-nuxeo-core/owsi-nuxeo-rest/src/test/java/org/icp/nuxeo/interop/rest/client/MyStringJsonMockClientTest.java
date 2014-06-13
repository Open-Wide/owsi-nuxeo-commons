package org.icp.nuxeo.interop.rest.client;

import java.io.IOException;

import org.apache.http.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.runtime.test.runner.Deploy;

import fr.openwide.nuxeo.test.AbstractWebEngineTest;


/**
 * Tests client of mock JSON server
 * @author mdutoo
 */
@Deploy("fr.openwide.nuxeo.commons.rest.mock")
public class MyStringJsonMockClientTest extends AbstractWebEngineTest {
   
   /**
    * Tests interoperability with M3P model
    * @throws HttpException
    * @throws IOException
    */
   @Test
   public void testGet() throws HttpException, IOException, Exception {
      MyStringClientBuilder myStringClientBuilder = new MyStringClientBuilder();
      //m3pClientBuilder.setM3pUrl(M3PClientBuilder.NUXEO_MOCK_URL); // is the default
      MyStringJsonApi api = myStringClientBuilder.constructApi();
      String stringForMyId = api.getStringForId("myId");
      Assert.assertEquals("myString", stringForMyId);
   }
   
}
