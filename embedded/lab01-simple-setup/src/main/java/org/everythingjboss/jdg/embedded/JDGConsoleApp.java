package org.everythingjboss.jdg.embedded;

import java.io.IOException;

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
				.cacheMode(CacheMode.LOCAL).build();

		EmbeddedCacheManager cacheManager = new DefaultCacheManager(configuration);
		Cache<String, String> cache = cacheManager.getCache("localCache");
		
		cache.put("1", "One");
		cache.put("2", "Two");
		logger.info("The size of the cache is : {}, mode of the cache is : {} ", cache.size(),
				cache.getCacheConfiguration().clustering().cacheMode());
		
		cacheManager.stop();
	}
}
