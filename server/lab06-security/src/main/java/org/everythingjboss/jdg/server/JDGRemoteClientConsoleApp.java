package org.everythingjboss.jdg.server;

import java.io.IOException;
import java.util.stream.IntStream;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDGRemoteClientConsoleApp {

    private static final Logger logger =  LoggerFactory.getLogger(JDGRemoteClientConsoleApp.class);

    public static void main(String[] args) throws IOException {

        // Build the cache configuration and instantiate a remote cache
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.security().authentication()
            .enable()
            .serverName("hotrodserver")
            .saslMechanism("DIGEST-MD5")
            .callbackHandler(new MyCallbackHandler("dguser", "ApplicationRealm", "dguser1!".toCharArray()));

        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, String> secureCache = remoteCacheManager.getCache("secureCache");

        IntStream.rangeClosed(1, 1000).parallel().forEach( i -> {
            secureCache.put("key"+i, "value"+i);
        });

        logger.info("Total number of entries writter to the secure cache is {}",secureCache.size());
        
        remoteCacheManager.stop();
    }
}