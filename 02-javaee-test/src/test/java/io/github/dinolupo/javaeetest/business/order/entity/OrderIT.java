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
package io.github.dinolupo.javaeetest.business.order.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class OrderIT {

    private EntityManager em;
    private EntityTransaction tx;
    
    @Before
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("integration-test");
        this.em = emf.createEntityManager();
        this.tx = this.em.getTransaction();
    }
    
    @Test
    public void verifyMappings() {
        this.tx.begin();
        this.em.merge(new Order("42"));
        this.tx.commit();
    }
    
}
