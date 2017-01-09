## lab04-listeners-notification (embedded)

### Lab Description

This lab demonstrates how to instantiate a simple **JBoss Data Grid** cache on which we can listen to any Cache or Cache Manager related events ([documentation](https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/7.0/html-single/Developer_Guide/index.html#chap-The_NotificationListener_API)).

In our example we will listen to both kind of events:
1. Cluster view (Cache Manager) events that let us know if a node joined or left the cluster
2. Entries added (Cache) event, that lets us know what entries got added to the cache

#### Attaching a listener to Cache or CacheManager instance

Once you define the listener as done in [ClusterListener.java](src/main/java/org/everythingjboss/jdg/embedded/ClusterListener.java) then listen to cache/cache manager events using the following API calls:

```java
...
cache.addListener(new ClusterListener());
...
cacheManager.addListener(new ClusterListener());
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
09:39:46.806 [main] INFO  org.everythingjboss.jdg.embedded.JDGConsoleApp - *** This is the coordinator instance ***
09:39:51.044 [Incoming-2,localhost-33011] INFO  org.everythingjboss.jdg.embedded.ClusterListener - The total # of nodes up so far is is : 2
09:39:51.044 [Incoming-2,localhost-33011] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Still waiting for at least 3 nodes to be up
09:39:53.207 [Incoming-2,localhost-33011] INFO  org.everythingjboss.jdg.embedded.ClusterListener - The total # of nodes up so far is is : 3
09:39:54.239 [remote-thread--p2-t1] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key3
09:39:54.241 [ForkJoinPool.commonPool-worker-6] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key1
09:39:54.242 [ForkJoinPool.commonPool-worker-0] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key6
09:39:54.242 [ForkJoinPool.commonPool-worker-4] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key8
09:39:54.245 [remote-thread--p2-t1] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key9
09:39:54.245 [remote-thread--p2-t2] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key7
09:39:54.248 [remote-thread--p2-t2] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key2
09:39:54.255 [ForkJoinPool.commonPool-worker-7] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key5
09:39:54.255 [ForkJoinPool.commonPool-worker-5] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key10
```
You should see a output similar to the following output on the rest of the nodes :

```sh
09:39:54.250 [remote-thread--p2-t4] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key5
09:39:54.250 [remote-thread--p2-t3] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key10
09:39:54.250 [remote-thread--p2-t5] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key2
09:39:54.250 [remote-thread--p2-t2] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key7
09:39:54.250 [remote-thread--p2-t1] INFO  org.everythingjboss.jdg.embedded.ClusterListener - Cache Entry created with key key4
```

### Testing/Verification

Now verify using **jconsole** how many entries exists in each instance. In JConsole traverse the path *org.infinipsan → cache → distCache → DefaultCacheManager → Statistics → Attributes → numberOfEntries*. This could be very helpful in verifying **distribution** of entries. 

> Note: Since in the code we do not end with cacheManager.stop(), the process keeps running until forcefully terminated at command line with CNTRL+C. This was intentional to give us adequate time to verify entries thru JConsole
