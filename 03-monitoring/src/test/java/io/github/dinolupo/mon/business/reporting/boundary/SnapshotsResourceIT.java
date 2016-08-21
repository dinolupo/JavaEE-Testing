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
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class SnapshotsResourceIT extends JerseyTest {

    // we need to Override this method to configure the system
    @Override
    protected Application configure() {
        // because we are not in a container, we need to configure the system
        return new ResourceConfig(SnapshotsResource.class);
    }
    
    
    @Test
    public void snapshots() {
        Response response = target("snapshots").request().get();
        assertThat(response.getStatusInfo(), is(Status.OK));
        JsonObject result = response.readEntity(JsonObject.class);
        assertNotNull(result);
        System.out.println("result = " + result);
    }
    
}
