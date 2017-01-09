package org.everythingjboss.jdg.embedded;

import java.io.IOException;
import java.util.stream.IntStream;

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

    public static void main(String[] args) throws IOException {
        
        // Common Global configuration with a default transport 
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .transport()
                .defaultTransport()
                .build();
        
        // Define configuration for Distributed cache in Synchronous mode
        Configuration config = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .eviction()
                    .size(50)
                    .strategy(EvictionStrategy.LIRS)
                    .type(EvictionType.COUNT)
                .persistence()
                    .passivation(true)
                    .addSingleFileStore()
                        .maxEntries(5000)
                        .location(System.getProperty("cacheStorePath"))
                .build();            

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig,config);
        Cache<String, String> cache = cacheManager.getCache("persistentCache");
        
        IntStream.range(1, 101).parallel().forEach( i -> cache.put("key"+i, "value"+i));
        
        logger.info("The size of the cache is : {}, mode of the cache is : {} ", cache.size(),
                cache.getCacheConfiguration().clustering().cacheMode());
    }
}
