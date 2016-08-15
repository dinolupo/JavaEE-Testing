/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dinolupo.junit.demo;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Arrays;
import java.util.List;
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
     
}
