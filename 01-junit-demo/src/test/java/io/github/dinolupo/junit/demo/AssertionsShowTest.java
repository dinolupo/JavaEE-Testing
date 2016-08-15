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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author dino
 */
public class AssertionsShowTest {

    private List<String> list;
    
    public AssertionsShowTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.list = Arrays.asList("java", "javaee", "joker");
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void list() {
         assertThat(list, hasItem("java"));
         assertThat(list, hasItems("java","joker"));
         assertThat(list, everyItem(containsString("j")));
     }
     
     @Test
     public void combinableMatchers() {
         assertThat(list, both(hasItem("java")).and(hasItem("joker")));
         assertThat(list, either(hasItem("java")).or(hasItem("javascript")));
         assertThat(list, anyOf(hasItem("java"), hasItem("javaee")));
         assertThat(list, allOf(hasItem("java"), hasItem("javaee")));
         assertThat(list, allOf(hasItem("java"), not(hasItem("erlang"))));
     }

     @Test
     public void customMatcher() {
         
         Matcher<String> containsJ = new CustomMatcher<String>("contains j") {
             
             @Override
             public boolean matches(Object item) {
                 if (!(item instanceof String)) {
                     return false;
                 }
                 String content = (String) item;
                 return content.contains("j");
             }
         };
         
         assertThat(list, everyItem(containsJ));
     }
     
}
