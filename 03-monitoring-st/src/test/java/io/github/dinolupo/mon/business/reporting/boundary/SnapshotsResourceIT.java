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

import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Rule;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class SnapshotsResourceIT {

    @Rule
    public JAXRSClient provider = JAXRSClient.target("http://localhost:8080/03-monitoring/resources/snapshots");

    @Test
    public void snapshots() {
        Response response = this.provider.target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(), is(Status.OK));
        JsonObject entity = response.readEntity(JsonObject.class);
        assertNotNull(entity);
        System.out.println("entity = " + entity);
    }

    @Test
    public void crud() {
        final String key = "javaee";
        final String value = "testing";
        JsonObject payload = Json.createObjectBuilder()
                .add(key, value)
                .build();

        Response response = this.provider.target.request()
                .post(Entity.json(payload));

        assertThat(response.getStatusInfo(), is(Status.CREATED));

        // verify that a location has been returned
        String uri = response.getHeaderString("Location");
        assertNotNull(uri);
        System.out.println("uri = " + uri);

        // find the object with get and test its correctness
        JsonObject result = this.provider.client.target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        assertNotNull(result);
        assertThat(result.getString(key), is(value));
    }

    @Test
    public void findNotExisting() {
        String key = UUID.randomUUID().toString();
        Response response = this.provider.target
                .path(key)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatusInfo(), is(Status.NO_CONTENT));
    }

}
