package com.msa.api.regcovery.endpoint;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Service operation endpoint.
 */
@Path("/")
public class ApiResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/call/{name}")
    public Map<String, Object> hello(@PathParam("name")String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "1");
        map.put("codeMsg", "success");
        map.put("result", "hello, " + name);
        return map;
    }
}
