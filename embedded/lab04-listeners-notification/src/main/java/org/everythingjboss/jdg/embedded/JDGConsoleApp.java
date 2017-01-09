package org.everythingjboss.jdg.embedded;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class JDGConsoleApp {

    private static final Logger logger = LogManager.getLogger(JDGConsoleApp.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .transport()
                .defaultTransport()
                .build();

        Configuration distConfig = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .build();
        
        CountDownLatch cdl = new CountDownLatch(1);
        
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig);
        cacheManager.defineConfiguration("distCache", distConfig);
        Cache<String, String> cache = cacheManager.getCache("distCache");

        cache.addListener(new ClusterListener());

        if (cacheManager.isCoordinator()) {
            logger.info("*** This is the coordinator instance ***");
            cacheManager.addListener(new ClusterListener(cdl));
            
            // Suspend execution here till there are three nodes in the cluster
            cdl.await();
            
            // Necessary to wait for clustering to do its magic before we put in 10 entries
            Thread.sleep(1000);

            // Put 10 entries in the clustered cache
            IntStream.range(1, 11).parallel().forEach( i -> cache.put("key"+i, "value"+i));
        }
    }
}
