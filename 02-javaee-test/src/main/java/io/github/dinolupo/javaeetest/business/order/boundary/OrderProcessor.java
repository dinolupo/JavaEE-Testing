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
import io.github.dinolupo.javaeetest.business.order.entity.Order;
import javax.ejb.Stateless;
import javax.inject.Inject;


/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 * 
 * We will test POJO, so there isn't a JavaEE environment.
 * POJO will initialize fields in the constructor of the class
 * 
 */
@Stateless
public class OrderProcessor {
    
    @Inject
    PaymentProcessor paymentProcessor;
    
    @Inject
    LegacyAuthenticator legacyAuthenticator;

    @Inject
    OrderHistory history;
    
    public void order(String trackingNumber) {
        if (!legacyAuthenticator.authenticate()) {
            throw new SecurityException("not authenticated");
        }
        Order order = new Order(trackingNumber);
        paymentProcessor.pay();
        history.save(order);
        
    }
}
