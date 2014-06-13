package org.icp.nuxeo.interop.rest.client;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Only (Nuxeo) client-side interface
 * NB. Jersey doesn't support @GET and @POST on the same method
 * @author mdutoo
 *
 */
@Path("/")
////@Consumes(MediaType.APPLICATION_JSON) // only for methods with JSON input, else ex. mere HTTP GET is application/octet-stream on Jersey
@Produces(MediaType.APPLICATION_JSON)
public interface MyStringJsonApi {

   /**
    * Same as POST, but Jersey doesn't support @GET and @POST on the same method
    * @param projectId
    */
   @Path("/{id}")
   @GET
   public String getStringForId(@PathParam("id") String id);

   /**
    * Gets M3P project requirements evaluations, creates project if missing
    * @param projectId
    */
   @Path("/{id}")
   @POST
   public String postStringForId(@PathParam("id") String id);
   
}
