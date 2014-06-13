package org.icp.nuxeo.interop.rest.client;

import fr.openwide.nuxeo.rest.ClientBuilder;


/**
 * Helper for building REST clients of M3P server
 * 
 * @author mdutoo
 */
public class MyStringClientBuilder extends ClientBuilder<MyStringJsonApi> {

   //public static final String NUXEO_SITE_MOCK_URL = "http://localhost:8080/nuxeo/site/restmock";
   public static final String NUXEO_MOCK_URL = "http://localhost:8082/mock/myString";
   public static final String LOCAL_URL = "http://localhost:8081/myString";

   public MyStringClientBuilder() {
      this.setUrl(NUXEO_MOCK_URL);
      
      //this.singletons.add(new MyJsonMessageReader());
      //this.singletons.add(new MyJsonMessageWriter());

      /* NOT USED rather custom (or for write ???) 
      // Jackson's provider :
      //ObjectMapper mapper = new ObjectMapper(); // allows to configure Jackson
      JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(); // (mapper) from jackson-jaxrs
      singletons.add(jacksonJsonProvider);
      */
   }

}
