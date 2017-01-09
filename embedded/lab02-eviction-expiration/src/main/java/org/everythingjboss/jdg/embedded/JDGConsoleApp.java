package org.everythingjboss.jdg.embedded;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class JDGConsoleApp {

    private static final Logger logger = LogManager.getLogger(JDGConsoleApp.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.LOCAL)
                .eviction()
                    .type(EvictionType.COUNT)
                    .size(50)
                    .strategy(EvictionStrategy.LIRS)
                .expiration()
                    .lifespan(10, TimeUnit.SECONDS)
                    .wakeUpInterval(100)
                .build();

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(configuration);
        Cache<String, String> cache = cacheManager.getCache("boundedCache");
        
        IntStream.range(1, 76).parallel().forEach( i -> cache.put("key"+i, "value"+i));

        // The size should be reported as 50 here as the rest 25 should be evicted out
        logger.info("The size of the cache before expiration is : {}, mode of the cache is : {} ", cache.size(),
                cache.getCacheConfiguration().clustering().cacheMode());

        Thread.sleep(11000);
        
        // The size should be reported as 0 here as the entries should have expired by now
        logger.info("The size of the cache after expiration is : {}, mode of the cache is : {} ", cache.size(),
                cache.getCacheConfiguration().clustering().cacheMode());
        
        cacheManager.stop();
    }
}
