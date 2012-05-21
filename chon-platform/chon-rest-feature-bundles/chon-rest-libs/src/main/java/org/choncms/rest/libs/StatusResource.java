package org.choncms.rest.libs;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * 
 */
@Path(StatusResource.PATH)
public class StatusResource {
	
	public static final String PATH = "test";
	
    @GET @Produces("text/plain")
    public String getStatus() {
        return "chon-rest-active";
    }
    
    @GET @Path("reload") @Produces("text/plain")
    public String reload() {
    	return "TODO: reload";
    }
}