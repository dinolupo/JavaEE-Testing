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
package io.github.dinolupo.junit.demo;

import static org.junit.Assert.fail;
import org.junit.Test;
import static org.junit.Assume.*;
import org.junit.Assume;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 * 
 * Skip tests (gray) with static methods of the Assume package.
 * 
 * Ignore annotation is forbidden because the test will be ignored forever
 * 
 */
public class AssumeTest {

    @Test
    public void assumeNoExceptionDemostration() {

        try {
            throw new IllegalStateException("Internet not working");
        } catch (Exception ex) {
            assumeNoException(ex);
        }
        
        fail("Should not arrive here");
        
    }

}
