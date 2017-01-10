## lab02-eviction-expiration (server)

### Prerequisite

The JDG server(s) should be started in domain mode and should be ready before the lab is executed. Please refer to the documentation [here](../../README.md) to start the JDG server(s) in domain mode.

Follow your instructor's instructions on how to scale the cluster from 2 nodes to 1 before you run the lab.

### Lab Description

This lab demonstrates how to work with a remote yet local (mode) **JBoss Data Grid** cache configured with eviction and expiry that is running in a server instance and notice its behavior. Refer to the [documentation](https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/7.0/html-single/Administration_and_Configuration_Guide/index.html#chap-Set_Up_Eviction) (sections 2 and 3 of JVM Memory Management)

#### Configuration
We first need to build (remote) configuration that is required to instantiate the **RemoteCacheManager**. The default configuration by default points to the server running locally (127.0.0.1) and at port (11222). 

```java
Configuration configuration = new ConfigurationBuilder().build();
...
RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
...
RemoteCache<String, String> remoteCache = cacheManager.getCache("boundedCache");
```
The `boundedCache` referenced above should be first be setup on the servers before running the client application in the lab. 

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
        <local-cache-configuration name="bounded">
            <eviction strategy="LRU" type="COUNT" size="50"/>
            <expiration lifespan="10000" interval="100"/>
        </local-cache-configuration>
        <local-cache name="boundedCache" configuration="bounded"/>
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
14:48:14.221 [main] INFO  org.everythingjboss.jdg.server.JDGRemoteClientConsoleApp - The size of the cache before expiration is : 50
14:48:25.222 [main] INFO  org.everythingjboss.jdg.server.JDGRemoteClientConsoleApp - The size of the cache after expiration is : 0
```