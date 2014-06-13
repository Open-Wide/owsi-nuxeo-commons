package fr.openwide.nuxeo.commons.rest.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;

/**
 * Simulates a JSON server by returning data from a sample JSON file
 * 
 * @author mdutoo
 */
@Path("/mock")
//@Consumes(MediaType.APPLICATION_JSON) // only for methods with JSON input, else ex. mere HTTP GET is application/octet-stream on Jersey
@Produces(MediaType.APPLICATION_JSON)
public class JsonMockServer {
   
   public static final String PATH_PREFIX = "restmock/";

   /**
    * Same as POST, but Jersey doesn't support @GET and @POST on the same method
    * @param filePathWithoutExtension
    * @return
    */
   @Path("/{filePathWithoutExtension:.+}") // :.+ to accept even /
   @GET
   public String doGet(@PathParam("filePathWithoutExtension") String filePathWithoutExtension) {
      return doPost(filePathWithoutExtension);
   }

   /**
    * Simulates JSON REST operation
    * @param filePathWithoutExtension
    * @return
    */
   @Path("/{filePathWithoutExtension:.+}") // :.+ to accept even /
   @POST
   public String doPost(@PathParam("filePathWithoutExtension") String filePathWithoutExtension) {
      try {
         // HACK somehow : wouldn't work in perfect OSGi world where accessing
         // the Bundle (Activator) would be required instead
         String resourcePathInClassoader = PATH_PREFIX + filePathWithoutExtension + ".json";
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourcePathInClassoader);
         if (in == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND)
                     .entity(resourcePathInClassoader).build());
         }
         return IOUtils.toString(in);
         ///return FileUtils.readFileToString(new File("src/main/resources/" + "restmock/" + filePathWithoutExtension + ".json"));
      } catch (IOException e) {
         throw new WebApplicationException(e);
      }
   }
   
}
