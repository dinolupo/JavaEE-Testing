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
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *
 * @author Dino Lupo <https://dinolupo.github.io>
 */
public class EntityManagerProvider implements TestRule {

    EntityManager em;
    EntityTransaction tx;

    private EntityManagerProvider(String unitName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName);
        this.em = emf.createEntityManager();
        this.tx = this.em.getTransaction();
    }

    public static EntityManagerProvider withUnit(String name) {
        return new EntityManagerProvider(name);
    }
    
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement(){
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                em.clear();
            }
        };
    }
    
}
