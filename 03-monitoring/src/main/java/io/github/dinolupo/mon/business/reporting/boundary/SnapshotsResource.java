/*
 * Copyright 2016 Dino Lupo <https://dinolupo.github.io>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.dinolupo.mon.business.reporting.boundary;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
@Singleton //used for testing purposes
@Path("snapshots")
public class SnapshotsResource {

    // simulate a no sql db to store saved json object
    private ConcurrentHashMap<String, JsonObject> noSQLDB;
    
    @PostConstruct
    public void init() {
        this.noSQLDB = new ConcurrentHashMap<>();
    }
    
    @GET
    public JsonObject snapshots() {
        return Json.createObjectBuilder()
                .add("s1", "fast")
                .build();        
    }
    
    @GET
    @Path("{id}")
    public JsonObject findSnapshot(@PathParam("id") String uuid) {
        return noSQLDB.get(uuid);
    }
    
    @POST
    public Response save(JsonObject payload, @Context UriInfo uriInfo) {
        String uuid = UUID.randomUUID().toString();
        
        // placeholder for storing object to DB
        noSQLDB.put(uuid, payload);
        
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path("/" + uuid)
                .build();
        return Response.created(uri).build();
    }
    
}
