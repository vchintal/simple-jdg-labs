package org.everythingjboss.jdg.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class JDGRemoteClientConsoleApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new ConfigurationBuilder().build();
        CountDownLatch cdl = new CountDownLatch(1);
        
        RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
        RemoteCache<String, String> remoteCache = cacheManager.getCache("listenerCache");
        remoteCache.addClientListener(new ClusteredClientListener());
        cdl.await();
    }
}
