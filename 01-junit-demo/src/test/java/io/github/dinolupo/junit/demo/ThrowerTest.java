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

import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class ThrowerTest {
    
    Thrower cut;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        cut = new Thrower();
    }
    
    /**
     * Test of throwsException method, of class Thrower.
     */
    @Test(expected = IllegalStateException.class)
    public void genericException() {
        System.out.println("throwsException");
        cut.throwsException();
    }
    
    @Test
    public void detailedException() {
        try {
            this.cut.throwsException();
            fail("Expecting IllegalStateException");
        } catch(IllegalStateException ex) {
            String message = ex.getMessage();
            assertThat(message, containsString("illegal"));
        }
    }
    
    @Test
    public void exceptionWithRule() {
        this.thrown.expect(IllegalStateException.class);
        this.thrown.expectMessage(containsString("illegal"));
        this.cut.throwsException();
    }
    
}
