package org.everythingjboss.jdg.embedded;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.swing.text.GapContent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class JDGConsoleApp {

    private static final Logger logger = LogManager.getLogger(JDGConsoleApp.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Common Global configuration with a default transport 
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .transport()
                .defaultTransport()
                .build();
        
        // Define configuration for Replicated cache in Synchronous mode
        Configuration replConfig = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .build();
        
        // Define configuration for Distributed cache in Synchronous mode
        Configuration distConfig = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .build();

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig);
        
        // Use the following two lines only when using the programmatic configuration
        cacheManager.defineConfiguration("replCache", replConfig);
        cacheManager.defineConfiguration("distCache", distConfig);
        
        Cache<String, String> cache = cacheManager.getCache("distCache");

        if(cacheManager.isCoordinator()) {
            logger.info("*** This node is the coordinator ***");
            IntStream.range(1, 101).parallel().forEach( i -> cache.put("key"+i, "value"+i));
        }

        logger.info("The size of the cache is : {}, mode of the cache is : {} ", cache.size(),
                cache.getCacheConfiguration().clustering().cacheMode());
    }
}
