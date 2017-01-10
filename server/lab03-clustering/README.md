## lab03-clustering (server)

### Prerequisite

The JDG server(s) should be started in domain mode and should be ready before the lab is executed. Please refer to the documentation [here](../../README.md) to start the JDG server(s) in domain mode.

Follow your instructor's instructions on how to scale the cluster from 1 node to multiple nodes before you run the lab.

### Lab Description

This lab demonstrates how to work with a remote **JBoss Data Grid** cache that can either be REPLICATED or DISTRIBUTED and notice the behavior. 

#### Configuration
We first need to build (remote) configuration that is required to instantiate the **RemoteCacheManager**. The default configuration by default points to the server running locally (127.0.0.1) and at port (11222). 

```java
Configuration configuration = new ConfigurationBuilder().build();
...
RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
...
RemoteCache<String, String> remoteCache = cacheManager.getCache("replCache");
```
The `replCache` or `distCache` referenced above should be first be setup on the servers before running the client application in the lab. 

### Executing the lab

#### Setup cache(s)

The instruction to deploy the `boundedCache` is in the file [addCaches](cli/addCaches.cli). To execute the CLI (command line interface) script, execute the command as shown below

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
        <replicated-cache-configuration name="replicated" mode="SYNC" start="EAGER"/>
        <replicated-cache name="replCache" configuration="replicated"/>
        <distributed-cache-configuration name="distributed" mode="SYNC"/>
        <distributed-cache name="distCache" configuration="distributed"/>    
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
15:01:49.935 [main] INFO  org.everythingjboss.jdg.server.JDGRemoteClientConsoleApp - The size of the cache is : 100
```

#### Testing/Verification

Follow the instructor on how to verify the replication and distribution of the 100 entries in both the `replCache` and `distCache` from the JDG management console.

Now verify using jconsole how many entries exists in each instance. In JConsole traverse the path _jboss.datagrid-infinispan → Cache → distCache → clustered → Statistics → Attributes → numberOfEntries_. This could be very helpful in verifying distribution of entries.