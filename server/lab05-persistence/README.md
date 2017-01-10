## lab05-persistence (server)

### Prerequisite

The JDG server(s) should be started in domain mode and should be ready before the lab is executed. Please refer to the documentation [here](../../README.md) to start the JDG server(s) in domain mode.

### Lab Description

This lab demonstrates how to work with a remote **JBoss Data Grid** cache that is configured with persistence using a file based store. Refer to the [documentation](https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/7.0/html-single/Administration_and_Configuration_Guide/index.html#sect-Single_File_Cache_Store) for more details. 

There is usually no change to how the clients interact with the cache. The change is only in the way how the cache is configured.

The lab should be run/tested in three ways with the cache configuration having:
1. No eviction (eviction configuration should be commented out)
2. Eviction with no passivation (passivation set to false)
3. Eviction with passivation 

### Executing the lab

#### Setup cache(s)

The instruction to deploy the `persistentCache` and `persistentPassivatedCache` is in the file [addCaches](cli/addCaches.cli). To execute the CLI (command line interface) script, execute the command as shown below

```sh 
# Run from the root folder of the lab
$JDG_HOME/bin/cli.sh -c --file=cli/addCaches.cli
```

This will deploy the cache configuration in the file **demo-domain.xml** as shown below:

```xml
<subsystem xmlns="urn:infinispan:server:core:8.3">
...
    <cache-container name="clustered" default-cache="default" statistics="true">
        ...
        <distributed-cache-configuration name="persistent-file-store" mode="SYNC" start="EAGER">
            <file-store shared="false" passivation="false" fetch-state="true"/>
        </distributed-cache-configuration>
        <distributed-cache-configuration name="persistent-file-store-passivation" mode="SYNC" start="EAGER">
            <eviction strategy="LRU" type="COUNT" size="10000"/>
            <file-store shared="false" passivation="true" fetch-state="true">
                <write-behind modification-queue-size="1024" thread-pool-size="1"/>
            </file-store>
        </distributed-cache-configuration>
        <distributed-cache name="persistentCache" configuration="persistent-file-store"/>          
        <distributed-cache name="persistentPassivatedCache" configuration="persistent-file-store-passivation"/>
        ...
    </cache-container>
...
</subsystem>
```

#### Run client application

After you have perused the client code thoroughly, run the following command to execute the client code.

```sh 
mvn clean compile exec:exec
```

### Output

You should see the following output on the node:

```sh
15:59:49.401 [main] INFO  org.everythingjboss.jdg.server.JDGRemoteClientConsoleApp - The size of the persistentCache cache is : 10500 and of persistentPassivatedCache cache is 10500
```

### Testing/Verification

Follow the instuctor's guidance on how to verify how the two caches behave when 10500 entries are put in both the caches. Using the CLI script provided one can test #1 and #3 persistence use cases mentioned in the Lab Description.

To test the #2 use case, persistence with eviction, run the following CLI script:

```sh
/profile=clustered/subsystem=datagrid-infinispan/cache-container=clustered/configurations=CONFIGURATIONS/distributed-cache-configuration=persistent-file-store/eviction=EVICTION:add(size=1000,type=COUNT,strategy=LRU)
:reload-servers
```

Run the client application again and verify the entries of `persistentCache` now with eviction enabled.

#### Outcomes

1. **No Eviction**
   * There will be 10500 entries in the `persistentCache` and 10500 entries stored in the store
2. **Eviction but no Passivation**
   * There will be 10000 entries in the `persistentCache` (after eviction is enabled) and 10500 entries stored in the store
3. **Eviction with Passivation**
   * There will be 10000 entries in the `persistentPassivatedCache` and the rest (mutually exclusive set) 500 in the store