## lab04-listeners-notification (server)

### Prerequisite

The JDG server(s) should be started in domain mode and should be ready before the lab is executed. Please refer to the documentation [here](../../README.md) to start the JDG server(s) in domain mode.

Follow your instructor's instructions on how to scale the cluster from 1 node to multiple nodes (as and if required) before you run the lab.

### Lab Description

This lab demonstrates how to listen to cache events on a remote **JBoss Data Grid** cache. Refer to the [documentation](https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/7.0/html-single/Developer_Guide/index.html#sect-Remote_Event_Listeners_Hot_Rod) here for more details.

#### Configuration
We first need to build (remote) configuration that is required to instantiate the **RemoteCacheManager**. The default configuration by default points to the server running locally (127.0.0.1) and at port (11222). 

```java
Configuration configuration = new ConfigurationBuilder().build();
RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
RemoteCache<String, String> remoteCache = cacheManager.getCache("listenerCache");
remoteCache.addClientListener(new ClusteredClientListener());
```
The `listenerCache` referenced above should be first be setup on the servers before running the client application in the lab. 

### Executing the lab

#### Setup cache(s)

The instruction to deploy the `listenerCache` is in the file [addCaches](cli/addCaches.cli). To execute the CLI (command line interface) script, execute the command as shown below

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
        <replicated-cache-configuration name="listener" mode="SYNC">
            <compatibility enabled="true"/>
        </replicated-cache-configuration>
        <replicated-cache name="listenerCache" configuration="listener"/>
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

> Note: The application is deliberatly suspended just to demonstrate the listener functionality. Press CNTRL+c to kill the process after you are done verifying.

#### Testing/Verification

Using a REST tool such as [RESTClient](https://addons.mozilla.org/en-US/firefox/addon/restclient/) submit a cache put with the following settings and verify the output:

* URL: http://127.0.0.1:8080/rest/listenerCache/1
* Method: **PUT** 
* Body: One

#### Output

You should see the output similar to the following on the node:

```sh
15:37:24.378 [Client-Listener-listenerCache-6c98e872c4c74cd4] INFO  org.everythingjboss.jdg.server.ClusteredClientListener - Cache Entry created with key 1
```