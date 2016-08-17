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
package io.github.dinolupo.javaeetest.business.order.boundary;

import io.github.dinolupo.javaeetest.business.order.control.LegacyAuthenticator;
import io.github.dinolupo.javaeetest.business.order.control.PaymentProcessor;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class OrderProcessorTest {
    
    OrderProcessor cut;
    
    @Before
    public void init(){
        this.cut = new OrderProcessor();
        // when testing Java EE it is even more simple than testing POJOs, 
        // you have only to initialize and mock everything and you do not have to
        // override initialization code in POJO constructors 
        this.cut.legacyAuthenticator = mock(LegacyAuthenticator.class);
        this.cut.paymentProcessor = mock(PaymentProcessor.class);
        this.cut.history = mock(OrderHistory.class);
    }
 
    @Test
    public void successfulOrder(){
        when(this.cut.legacyAuthenticator.authenticate()).thenReturn(Boolean.TRUE);
        this.cut.order("42");
        verify(this.cut.paymentProcessor, times(1)).pay();
        // we do not need to interact with EntityManager because we have mocked the OrderHistory
        // and we only need to verify that the save() method is called
        verify(this.cut.history).save(anyObject());
    }
    
    
    // how to test for invalid user?
    @Test(expected = SecurityException.class)
    public void invalidUser() {
        when(this.cut.legacyAuthenticator.authenticate()).thenReturn(Boolean.FALSE);
        this.cut.order("42");
    }
    
    
}
 