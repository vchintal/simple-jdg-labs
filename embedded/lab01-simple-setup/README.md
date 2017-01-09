## lab01-simple-setup (embedded)

### Lab Description

This lab demonstrates how to instantiate a simple **JBoss Data Grid** cache and use the **embedded** mode APIs. 

The focus should be on the two modes of instantiating a cache:
1. Programmatically 
2. Declaratively 

#### Programmatic Way
Define a new cache configuration and pass it to the **DefaultCacheManager** as shown below:

```java
Configuration configuration = new ConfigurationBuilder()
        .clustering()
        .cacheMode(CacheMode.LOCAL)
        .build();
...
EmbeddedCacheManager cacheManager = new DefaultCacheManager(configuration);
...
```
#### Declarative Way
Define an XML file **infinispan.xml** as provided to you in resources folder and pass it to the **DefaultCacheManager** as shown below. Notice how you have to define the cache-configuration and then instantiate a cache using that defined configuration.

```xml
<infinispan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="urn:infinispan:config:8.2 http://www.infinispan.org/schemas/infinispan-config-8.2.xsd"
    xmlns="urn:infinispan:config:8.2">
    <cache-container default-cache="default">
        <local-cache-configuration name="local" />
        <local-cache name="localCache" configuration="local" />
    </cache-container>
</infinispan>
```

```java
...
EmbeddedCacheManager cacheManager = new DefaultCacheManager("infinispan.xml");
...
```

### Executing the lab

After you have perused the source file thoroughly, execute the project at command prompt using the following command:

```sh 
mvn clean compile exec:exec
```

### Output

You should see the following output on the node:

```sh
10:13:01.850 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache is : 2, mode of the cache is : LOCAL
```