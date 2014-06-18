package fr.openwide.nuxeo.rest;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;


/**
 * Helper for building REST clients whatever the JAXRS service
 * 
 * @author mdutoo
 */
public class ClientBuilder<T> {

   protected Set<Object> singletons = new HashSet<Object>();
   private String url;
   private String username = "Administrator";
   private String password = "Administrator";

   public ClientBuilder() {
      /* (do it in inheriting class) ex. : 
      // Jackson's provider :
      //ObjectMapper mapper = new ObjectMapper(); // allows to configure Jackson
      JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(); // (mapper) from jackson-jaxrs
      singletons.add(jacksonJsonProvider);
      */
   }

   public void addSingleton(Object singleton) {
      this.singletons.add(singleton);
   }

   public void setCredentials(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public ClientConfig constructClientConfig() {
      ClientConfig clientConfig = new DefaultClientConfig();
      clientConfig.getSingletons().addAll(this.singletons);
      return clientConfig;
   }

   public WebResource constructClientBase() {
      return createClient().resource(this.url);
   }

   public T constructApi() {
      WebResource client = constructClientBase();
      @SuppressWarnings("unchecked")
      Class<T> clazz = ((Class<T>) ((ParameterizedType) getClass()
               .getGenericSuperclass()).getActualTypeArguments()[0]);
      return WebResourceFactory.newResource(clazz, client);
   }

   /**
    * Creates an all-purpose, HTTP Basic-authenticated REST HTTP client to Nuxeo
    * (prefer specific ones if you can)
    * @return
    */
   public Client createClient() {
      Client client = Client.create(constructClientConfig());
      if (username != null && username.length() != 0) {
         client.addFilter(new HTTPBasicAuthFilter(username, password));
      }
      return client;
   }

   /**
    * To be used with createClient()'s all-purpose REST HTTP client
    * @return EasySOA Registry service URLs
    * @param c
    * @return
    */
   public String getURL(Class<?> c) {
      return this.url + PathExtractor.getPath(c);
   }

   /**
    * To be used with createClient()'s all-purpose REST HTTP client
    * @return EasySOA Registry service URLs
    * @param c
    * @param methodName
    * @param parameterTypes
    * @return
    * @throws SecurityException
    * @throws NoSuchMethodException
    */
   public String getURL(Class<?> c, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
      return this.url + PathExtractor.getPath(c, methodName, parameterTypes);
   }


   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

}
