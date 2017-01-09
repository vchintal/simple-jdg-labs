## lab02-eviction-expiration (embedded)

### Lab Description

This lab demonstrates how to instantiate a simple **JBoss Data Grid** cache where:
1. Eviction is enabled to retain only 50 entries at any given time
2. The eviction strategy is **LIRS** (refer to the documentation for more details)
3. Expiration is enabled to expire the entries every 10 seconds 
4. The wake up interval of the thread that clears the expired entries is set to 100 milli-seconds

#### Programmatic Way

```java
Configuration configuration = new ConfigurationBuilder()
        .clustering()
        .cacheMode(CacheMode.LOCAL)
        .eviction()
            .type(EvictionType.COUNT)
            .size(50)
            .strategy(EvictionStrategy.LIRS)
        .expiration()
            .lifespan(10, TimeUnit.SECONDS)
            .wakeUpInterval(100)
        .build();
...
EmbeddedCacheManager cacheManager = new DefaultCacheManager(configuration);
...
```

#### Declarative Way
Just pasting the cache configuration and the defined cache.

```xml
<cache-container default-cache="default">
    <local-cache-configuration name="local">
        <eviction type="COUNT" size="50" strategy="LIRS" />
        <expiration lifespan="10000" interval="100" />
    </local-cache-configuration>
    <local-cache name="boundedCache" configuration="local" />
</cache-container>
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

You should see the following output:

```sh
22:50:41.003 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache before expiration is : 50, mode of the cache is : LOCAL 
22:50:52.003 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache after expiration is : 0, mode of the cache is : LOCAL 
```