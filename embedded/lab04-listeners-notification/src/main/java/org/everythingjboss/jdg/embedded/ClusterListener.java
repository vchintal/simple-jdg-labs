package org.everythingjboss.jdg.embedded;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

@Listener
public class ClusterListener {
    private static final Logger logger = LogManager.getLogger(ClusterListener.class);

    private static final int MAX_NODES_UP = 3;
    private CountDownLatch cdl;

    public ClusterListener() {
    }

    public ClusterListener(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @ViewChanged
    public void viewChanged(ViewChangedEvent event) {
        logger.info("The total # of nodes up so far is is : " + event.getNewMembers().size());
        if (event.getCacheManager().isCoordinator() && event.getNewMembers().size() >= MAX_NODES_UP) {
            this.cdl.countDown();
        } else
            logger.info("Still waiting for at least " + MAX_NODES_UP + " nodes to be up");
    }

    @CacheEntryCreated
    public void cacheEntryCreated(CacheEntryCreatedEvent<String, String> event) {
        if (!event.isPre()) {
            logger.info("Cache Entry created with key " + event.getKey());
        }
    }
}
