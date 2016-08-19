# Java EE7 Testing sample code

This repository contains sample code for testing Java EE7 projects.

## Project folder `01-junit-demo`

### 01. Unit Test - The Definition

[http://c2.com/cgi/wiki?StandardDefinitionOfUnitTest]()

Basically the smallest unit to test is a method and it should be tested in isolation.

### 02. JUnit Intro

Folder `01-junit-demo` is a standard Mava Java project that is needed to show some basics of unit testing in Java.

> Maven pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>io.github.dinolupo</groupId>
    <artifactId>01-junit-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
</project>
```

### 03. Better Asserts with Hamcrest

With latest version of JUnit, hamcrest library is included with the jar, so there is no need to import as a Maven dependency.

### 04. Matching Collections

Matching collections is useful when we want to test JPA and classes that return List of Entities.

See `AssertionsShowTest.list()` 

### 05. Combining Matchers

Matchers can be combined using `both`, `either`, `anyOf` and `allOf`. 
See `AssertionsShowTest.combinableMatchers()`

### 06. Building a Custom Matcher

You can create a custom matcher, see `AssertionsShowTest.customMatcher()` that show how to create a Matcher that find a "j" character into a String.

### 07. Testing Exceptions

Methods that raise exceptions should be tested as well for exceptions. We have created a `Thrower` class to show it:

```java
public class Thrower {
    public void throwsException() {
        throw new IllegalStateException("illegal state");
    }    
}
```

See the class `ThrowerTest` for a showcase of how testing exceptions.

There are three methods to test for Exceptions: 

1) `genericException()`: using annotation parameter

```java
@Test(expected = IllegalStateException.class)
```

2) `detailedException()`: more detailed check with a check on the exception content

3) `exceptionWithRule()`: using Rule, check exception in a more concise way

```java

...

@Rule
public ExpectedException thrown = ExpectedException.none();

...

    @Test
    public void exceptionWithRule() {
        this.thrown.expect(IllegalStateException.class);
        this.thrown.expectMessage(containsString("illegal"));
        this.cut.throwsException();
    }

```

### 08. AOP For JUnit - Writing Custom Rules

With custom Rules, you can decorate a test (wrap it) and do something before and after it, this could be useful for example to provide EntityManager or JAX-RS client to a System Test.

Let's create a rule that prints something before and after the test:

```java
public class SystemOutRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("before Rule");
                base.evaluate();
                System.out.printf("after Rule ---> %s.%s() tested!\n",
						description.getClassName(), 
						description.getMethodName());
            }
        };
    }   
}
```

and then we use it in a Test to show how it works:

```java
public class HowWorksSystemOutRuleTest {
    
    @Rule
    public SystemOutRule rule = new SystemOutRule();
    
    @Test
    public void oneTest() {}
    
    @Test
    public void anotherTest() {}
}
```

The output will be as expected:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running io.github.dinolupo.junit.demo.HowWorksSystemOutRuleTest
before Rule
after Rule ---> io.github.dinolupo.junit.demo.HowWorksSystemOutRuleTest.oneTest() tested!
before Rule
after Rule ---> io.github.dinolupo.junit.demo.HowWorksSystemOutRuleTest.anotherTest() tested!
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.093 sec

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
```

### 09. Dealing With Slow Tests

When testing some methods that use networking or database you can check for timeouts. 

See the following test to show 2 ways to test for timeout:

```java
public class TimeoutTest {
    
    @Rule
    public Timeout timeout = new Timeout(1, TimeUnit.SECONDS);
    
    @Test(timeout = 2000)
    public void hello() throws InterruptedException {
        Thread.sleep(5000);
    }
    
    @Test
    public void veryLongTest() throws InterruptedException {
        Thread.sleep(5000);
    }
    
}
```

### 10. Skipping Tests with Assumptions

In case you do not want to execute a test method, you can use the methods of the Assume class. In case the assumptions are not satisfied, the test method is deactivated (gray in the Netbeans IDE console). For example let's suppose we are testing a method that should use a network connection, but the network is not available, then the test should be deactivated:

```java
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
```

If you run the test, the fail() statement is never reached because of the Assume. assumeNoException() statement.  

Note that **It is not nice to use `@Ignore` to skip a test!** because that test stays in the code forever and it is never executed, use Assumptions instead.

## Project folder `02-javaee-test`

We have created a simple Java EE project war that we use from now that act like a real project with JPA, JAX-RS services, etc.

### 11. Testing POJOs

In the `OrderProcessor` POJO we have two fields, a `LegacyAuthenticator` and a `PaymentProcessor`

Those two fields are initialized in a constructor so when we want to test the `OrderProcessor` we need to Mock those fields. We should change the visibility of the fields to `package` instead of `private` and create mocking classes.

### 12. An Intro Into Mocks

Mockito is a nice library that is useful to mock everything you need to test.

> Maven dependency for Mockito

```xml
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-all</artifactId>
            <version>1.10.19</version>
            <scope>test</scope>
        </dependency>
```

Our sample project has a LegacyAuthenticator class like the following:

```java
public class LegacyAuthenticator {
    public boolean authenticate() {
        // this is a placeholder for hard to test backend system 
        // like LDAP or something like that
        return true;
    }   
}
```

And a PaymentProcessor:

```java
public class PaymentProcessor {
    public void pay() {
        System.out.println("Payed!");
    }   
}
```

The Class Under Test (cut) is OrderProcessor:

```java
public class OrderProcessor {
    
    PaymentProcessor paymentProcessor;
    
    LegacyAuthenticator legacyAuthenticator;

	public OrderProcessor() {
		this.paymentProcessor = new PaymentProcessor();
		this.legacyAuthenticator = new LegacyAuthenticator();
	}

    public void order(String trackingNumber) {
        if (!legacyAuthenticator.authenticate()) {
            throw new SecurityException("not authenticated");
        }
        Order order = new Order(trackingNumber);
        paymentProcessor.pay();
    }
}
```

Our authenticator always returns true, so how to check the path that returns false? We use mockito to mock the Authenticator and then we use `when.thenReturn` to force authenticate() method to return `false`:  

> example with mocking class

```
public class OrderProcessorTest {

	OrderProcessor cut;

    @Before
    public void init(){
        this.cut = new OrderProcessor();
        this.cut.paymentProcessor = mock(PaymentProcessor.class);
    }

    @Test
    public void successfulOrder(){
        this.cut.order();
        verify(this.cut.paymentProcessor, times(1)).pay();
    }

    @Test(expected = SecurityException.class)
    public void invalidUser() {
        this.cut.legacyAuthenticator = mock(LegacyAuthenticator.class);
        when(this.cut.legacyAuthenticator.authenticate()).thenReturn(Boolean.FALSE);
        this.cut.order();
    }
}   
```

### 13. POJOs vs Java EE Testing

The main difference between POJOs and Java EE Components (EJB, CDI, etc.) is that Java EE requires less code to write. For example, the `OrderProcessor` does not need anymore the constructor and in the test class we need to ensure that every bean is mocked using Mockito. 

### 14. Mocking the JPA EntityManager

In Java EE projects we should test also JPA components, so let's create a sample class `OrderHistory` that save an Entity `Order`.

> OrderHistory class uses the `EntityManager` of the container, injected via the `@PersistenceContext` annotation:

```java
public class OrderHistory {

    @PersistenceContext
    EntityManager em;
    
    public void save(Order order) {
        em.merge(order);
    }   
}
```

> Order entity, we have a trackingNumber and a technical id that is auto generated:

```java
@Entity
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String trackingNumber;

    public Order(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Order() {
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
       
}
```

Our `OrderProcessor` should use the newly created class `OrderHistory` to save an `Order`:

> `OrderProcessor` is now an EJB and use the `OrderHistory`

```java
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
```

Now, what we are going to test is the OrderHistory, mocking it with mockito. **There is no need to mock the EntityManager**:

```java
public class OrderProcessorTest {
    
    OrderProcessor cut;
    
    @Before
    public void init(){
        this.cut = new OrderProcessor();
        // Testing Java EE it is even more simple than testing POJOs, 
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

}
```

Even if the save() methods would return a List of Orders, we can mock also the returned results.


### 15. Custom Argument Matchers

Since it is a bad practice using anyObject() in our previous test, we want to use a Matcher instead to verify that the correct Object is passed to the save method. Let's see the new successfulOrderWithMatcher() test created for this scope:

> new method that test the correctness of the Object passed to the save() method

```java
...
    @Test
    public void successfulOrderWithMatcher(){
        when(this.cut.legacyAuthenticator.authenticate()).thenReturn(Boolean.TRUE);
        final String expectedId = "42";
        this.cut.order(expectedId);
        verify(this.cut.paymentProcessor, times(1)).pay();
        // We use a custom matcher here, because we are not satisfied with anyObject(). 
        // We want to test that the Order entity is the argument and that the id is "42"
        verify(this.cut.history).save(argThat(new BaseMatcher<Order>() {
            
            @Override
            public boolean matches(Object item) {
                // the argument of save() has to be an Order
                if (!(item instanceof Order)) {
                    return false;
                }
                Order order = (Order) item;
                // check the correctness of the Order values
                return expectedId.equalsIgnoreCase(order.getTrackingNumber());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Should be an Order with id=" + expectedId);
            }
            
        }));
    }
...
```


### 16. Code Coverage Is Nice, But

Maven has a nice plugin for code coverage, called *jacoco*, that is also integrated in Netbeans (it appears after adding the plugin to the pom.xml)

> pom.xml plugin section for code coverage:

```xml
    <build>
        <!-- CODE COVERAGE PLUGIN -->
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.7.7.201606060606</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>prepare-integration-test-agent</id>
                        <goals>
                            <goal>prepare-agent-integration</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>generate-integration-test-report</id>
                        <goals>
                            <goal>report-integration</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <!-- END OF CODE COVERAGE PLUGIN -->
    </build>
```

In JavaEE, Code Coverage is useful to find code that is not used anymore. It's best to rely on System Test with package really depoloyed on an application server.

### 17. Integration Testing JPA Persistence

We tested a lot but we have no idea if the application is even deployable on application server, so before I deploy the first time the application, what I would like to do is to write a *test* to verify whether the Entity is deployable and mappable to a table.

In order to do this, let's create an Integration Test `OrderIT`.

In the @Before method let's use the Persistence methods to bootstrap the EntityManager outside the container and then retrieve a Transaction:

```java
public class OrderIT {

    private EntityManager em;
    private EntityTransaction tx;
    
    @Before
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("integration-test");
        this.em = emf.createEntityManager();
        this.tx = this.em.getTransaction();
    }   
}
```

Now we want to add a method that verifies mappings on a SQL Table:

```java
    @Test
    public void verifyMappings() {
        this.tx.begin();
        this.em.merge(new Order("42"));
        this.tx.commit();
    }
```

At this point we are relying on a Persistence Unit named "it" (Integration Testing) but in the project we have only a "prod" Persistence Unit. We do not want to use a production persistence unit, but we want to use one that is usable for testing purpouses, so we are going to create a new persistence.xml only for testing (in the testing tree):

```sh
mkdir src/test/resources/META-INF
```

And create a persistence.xml file with the following content:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
  <persistence-unit name="integration-test" transaction-type="RESOURCE_LOCAL">
    <class>io.github.dinolupo.javaeetest.business.order.entity.Order</class>
    <exclude-unlisted-classes>true</exclude-unlisted-classes>
    <properties>
      <property name="javax.persistence.jdbc.url" value="jdbc:derby:./testDB;create=true"/>
      <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.EmbeddedDriver"/>
      <property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>
    </properties>
  </persistence-unit>
</persistence>
```

We have inserted the class Order in the persistence unit and we want to use Derby as a test database. Also, since we are not going to test into an application server, we need an implementation for JPA, because in Maven we only have the javaee-api \<provided\> and no implementations.

So we need to import a JPA implementation (eclipselink) and Derby database:

> eclipselink

```xml
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>eclipselink</artifactId>
            <version>2.6.3</version>
            <scope>test</scope>
        </dependency>

``` 

> derby db

```xml
        <dependency>
            <groupId>org.apache.derby</groupId>
            <artifactId>derby</artifactId>
            <version>10.12.1.1</version>
            <scope>test</scope>
        </dependency>
```

Now, if you try to run tests, you will find an error, because "ORDER" is a SQL reserved word and cannot be used as a table name. You will need to add a custom table name as follows:

```java
...
@Entity(name = "T_ORDER")
public class Order implements Serializable {...}
```

To summarize, for every Entity we need to write an Integration Test to test mappings and queries without the need to use an application server. We can factor out the EntityManager construction so the test will be even more efficient.


### 18. Reusing JPA Initialization Code


### 19. Moving to Rulz


### 20. System Tests For JAX-RS


### 21. JAX-RS Test Rules


### 22. The Look and Feel of Embedded JAX-RS


### 23. System Tests Driven JAX-RS


### 24. Refining The Asserts


### 25. Embedded Tests Wth Arquillian


### 26. Embedded Tests With Delta Spike


### 27. Embedded Tests With CDIUnit


### 28. Test Support Classes--The Arquillian Killer


### 29. Testing Plugins--Another Arquillian Killer


### 30. Controlling Browser With Graphene


### 31. Chrome Setup For Graphene


### 32. More Convenience with Page Objects


### 33. Browser Testing--Is It Worth


### 34. Stress Testing With Apache Bench


### 35. Stress Tests With JMeter


### 36. Performance Tuning With JVisualVM


### 37. Application Server Monitoring


### 38. Directly Reusing System Tests as Stress Tests


### 39. Exposing Inaccessible Functionality for Stress Tests with Servlets


### 40. JSPs As Flexible Test Drivers


### 41. FIT For Complex Algorithms


### 42. Validating Code Coverage by Killing The Mu


### 43. Testing Asynchronous APIs


### 44. Providing Stage Dependent Test Configurati


### 45. HTTP Server Mocking


### 46. Smoke Tests With Ping


### 47. Benchmarking Logger Performance With JMH


### 48. Benchmarking JAX-RS Endpoints With JMH





