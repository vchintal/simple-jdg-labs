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

    private static final Logger logger = LoggerFactory.getLogger(JDGRemoteClientConsoleApp.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new ConfigurationBuilder().build();

        RemoteCacheManager cacheManager = new RemoteCacheManager(configuration);
        RemoteCache<String, String> remoteCache = cacheManager.getCache("boundedCache");

        IntStream.rangeClosed(1, 100) .parallel().forEach(i -> remoteCache.put("key" + i, "value" + i));

        logger.info("The size of the cache before expiration is : {}", remoteCache.size());
        logger.info("The keyset is : "+remoteCache.keySet());

        // Sleep beyond the lifespan of cache entries
        Thread.sleep(11000);

        logger.info("The size of the cache after expiration is : {}", remoteCache.size());
        cacheManager.stop();
    }
}
