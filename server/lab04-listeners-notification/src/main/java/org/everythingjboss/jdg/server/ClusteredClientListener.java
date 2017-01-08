package org.everythingjboss.jdg.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.client.hotrod.annotation.*;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;

@ClientListener
public class ClusteredClientListener {
	private static final Logger logger = LogManager.getLogger(ClusteredClientListener.class);

	@ClientCacheEntryCreated
	public void cacheEntryCreated(ClientCacheEntryCreatedEvent<String> event) {
		logger.info("Cache Entry created with key " + event.getKey());
	}
}
