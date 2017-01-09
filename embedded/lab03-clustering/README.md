## lab03-clustering (embedded)

### Lab Description

This lab demonstrates how to instantiate a simple **JBoss Data Grid** cache which clusters together when multiple instances of the same program have been launched. 

In this example we will start three instances of the same program once with a **REPLICATED** cache and once with **DISTRIBUTED** cache. 

#### Programmatic Way

Below is the snippet for a **REPLICATED** cache:

```java
GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
        .transport()
        .defaultTransport()
        .build();

Configuration replConfig = new ConfigurationBuilder()
        .clustering()
        .cacheMode(CacheMode.REPL_SYNC)
        .build();
...
EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig);
...
cacheManager.defineConfiguration("replCache", replConfig);
...
Cache<String, String> cache = cacheManager.getCache("replCache");
...
```

#### Declarative Way

Below is the contents of **infinispan.xml**.    

```xml
<infinispan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="urn:infinispan:config:8.2 http://www.infinispan.org/schemas/infinispan-config-8.2.xsd"
    xmlns="urn:infinispan:config:8.2">
    <jgroups>
        <stack-file name="external-file" path="jgroups.xml" />
    </jgroups>
    <cache-container>
        <transport stack="external-file" />
        <distributed-cache name="distCache" mode="SYNC" />
        <replicated-cache  name="replCache" mode="SYNC" />
    </cache-container>
</infinispan>
```

```java
...
EmbeddedCacheManager cacheManager = new DefaultCacheManager("infinispan.xml");
...
```

### Executing the lab

After you have perused the source file thoroughly, execute the project at command prompt using the following command. Run the command(s) in three different terminal sessions:

#### First node

```sh 
mvn clean compile exec:exec
```
#### All other nodes

```sh 
mvn exec:exec
```

### Output
You should see the following output on the node that is the coordinator :

```sh
23:32:18.341 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - *** This node is the coordinator ***
23:32:18.387 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache is : 100, mode of the cache is : REPL_SYNC
```

You should see a output similar to the following output on the rest of the nodes :

```sh
23:32:35.484 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache is : 100, mode of the cache is : REPL_SYNC 
```

### Testing/Verification

Now verify using **jconsole** how many entries exists in each instance. In JConsole traverse the path *org.infinipsan → cache → replCache → DefaultCacheManager → Statistics → Attributes → numberOfEntries*. This could be very helpful in verifying **distribution** of entries. 

> Note: Since in the code we do not end with cacheManager.stop(), the process keeps running until forcefully terminated at command line with CNTRL+C. This was intentional to give us adequate time to verify entries thru JConsole
