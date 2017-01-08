package org.everythingjboss.jdg.embedded;

import java.io.IOException;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class JDGConsoleApp {

	private static final Logger logger = LogManager.getLogger(JDGConsoleApp.class);

	public static void main(String[] args) throws IOException {
		Configuration configuration = new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.DIST_SYNC).build();

		EmbeddedCacheManager cacheManager = new DefaultCacheManager("infinispan.xml");
		Cache<String, String> cache = cacheManager.getCache("persistentCache");
		
		IntStream.range(1, 101).parallel().forEach( i -> cache.put("key"+i, "value"+i));
		
		logger.info("The size of the cache is : {}, mode of the cache is : {} ", cache.size(),
				cache.getCacheConfiguration().clustering().cacheMode());

	}
}
