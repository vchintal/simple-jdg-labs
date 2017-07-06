package org.everythingjboss.jdg.server;

import org.infinispan.client.hotrod.annotation.*;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientListener
public class ClusteredClientListener {
    private static final Logger logger =  LoggerFactory.getLogger(ClusteredClientListener.class);
    
    @ClientCacheEntryCreated
    public void cacheEntryCreated(ClientCacheEntryCreatedEvent<String> event) {
        logger.info("Cache Entry created with key " + event.getKey());
    }
}
