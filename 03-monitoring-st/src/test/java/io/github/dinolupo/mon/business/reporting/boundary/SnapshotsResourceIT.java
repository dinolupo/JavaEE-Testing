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

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class SnapshotsResourceIT {

    private Client client;
    // target under test
    private WebTarget tut;
    
    public SnapshotsResourceIT() {
    }
    
    @Before
    public void setUp() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/03-monitoring/resources/snapshots");
    }

     @Test
     public void snapshots() {
        Response response = this.tut.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(), is(Status.OK));
        JsonObject entity = response.readEntity(JsonObject.class);
         assertNotNull(entity);
         System.out.println("entity = " + entity);
     }
}
