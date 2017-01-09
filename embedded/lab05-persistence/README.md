## lab05-persistence (embedded)

### Lab Description

This lab demonstrates how to instantiate a simple **JBoss Data Grid** cache backed by a file based cache store for durability or just to offload entries ([documentation](https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/7.0/html-single/Administration_and_Configuration_Guide/index.html#Single_File_Store_Configuration_Library_Mode)).

There is usually no change to how the clients interact with the cache. The change is all in the how the cache is configured.

The lab should be run in three ways with the cache configuration having:
1. No eviction (eviction configuration should be commented out)
2. Eviction with no passivation (passivation set to false)
3. Eviction with passivation 

> Note: The count (max entries) in the eviction configuration is per node. So if after the distribution of entries, the total entries on a given node exceeds 50, only then eviction kicks in.  

### Persistence Configuration

#### Programmatic Way

```java
Configuration config = new ConfigurationBuilder()
        .clustering()
        .cacheMode(CacheMode.DIST_SYNC)
        .eviction()
            .size(50)
            .strategy(EvictionStrategy.LIRS)
            .type(EvictionType.COUNT)
        .persistence()
            .addSingleFileStore()
                .maxEntries(5000)
                .location(System.getProperty("cacheStorePath"))
        .build();   
```
#### Declarative Way

```xml
<distributed-cache-configuration
    name="persistentCacheConfiguration" mode="SYNC"
    statistics-available="true" statistics="true">
    <eviction max-entries="50" type="COUNT" />
    <persistence passivation="false">
        <file-store path="${cacheStorePath}"
            max-entries="5000" />
    </persistence>
</distributed-cache-configuration>
<distributed-cache name="persistentCache" configuration="persistentCacheConfiguration" />
```

### Executing the lab

After you have perused the source file thoroughly, execute the project at command prompt using the following command. Run the command(s) in three different terminal sessions:

#### First node

```sh 
mvn clean compile exec:exec -DcacheStorePath=/tmp/cacheStoreCoordinator
```
#### All other nodes

```sh 
mvn exec:exec -DcacheStorePath=/tmp/cacheStoreNodeN
```

### Output 
You should see the following output on the node that is the coordinator :

```sh
10:40:12.696 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - The size of the cache is : 100, mode of the cache is : DIST_SYNC 
```

### Testing/Verification

Now verify using **jconsole** how many entries exists in each instance. In JConsole traverse the path *org.infinipsan → cache → distCache → DefaultCacheManager → Statistics → Attributes → numberOfEntries*. This could be very helpful in verifying **distribution** of entries. 

> Note: Since in the code we do not end with cacheManager.stop(), the process keeps running until forcefully terminated at command line with CNTRL+C. This was intentional to give us adequate time to verify entries thru JConsole

#### Outcomes

1. **No Eviction**
   * There will be 100 entries in the cache and 100 entries stored in the store
2. **Eviction but no Passivation**
   * There will be 50 entries in the cache and 100 entries stored in the store
3. **Eviction with Passivation**
   * There will be 50 entries in the cache and the rest (mutually exclusive set) in the store


