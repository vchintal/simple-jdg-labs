package org.everythingjboss.jdg.server;

import java.io.IOException;
import java.util.stream.IntStream;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDGRemoteClientConsoleApp {

    private static final Logger logger =  LoggerFactory.getLogger(JDGRemoteClientConsoleApp.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new ConfigurationBuilder().build();

        RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
        RemoteCache<String, String> persistentCache = cacheManager.getCache("persistentCache");
        RemoteCache<String, String> persistentPassivatedCache = cacheManager.getCache("persistentPassivatedCache");

        IntStream.range(1, 10501).parallel().forEach( i -> {
            persistentCache.put("key"+i, "value"+i);
            persistentPassivatedCache.put("key"+i, "value"+i);
        });

        logger.info("The size of the {} cache is : {} and of {} cache is {}"
                ,"persistentCache", persistentCache.size()
                ,"persistentPassivatedCache", persistentPassivatedCache.size());
        cacheManager.stop();
    }
}
