package com.inno.course.observer;

import com.inno.course.entity.AbstractNumericArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Observer implementation that logs collection changes.
 * Useful for debugging and monitoring collection modifications.
 *
 * @author Your Name
 * @version 1.0
 * @see Observer
 * @see Observable
 * @see AbstractNumericArray
 */
public class CollectionChangeObserver implements Observer {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(CollectionChangeObserver.class.getName());

    /**
     * Handles collection change notifications by logging the event.
     *
     * @param collection the collection that changed (may be null in edge cases)
     */
    @Override
    public void update(AbstractNumericArray<?> collection) {
        if (collection != null) {
            logger.info("Collection change detected - ID: {}, Size: {}",
                    collection.getId(), collection.size());

            // Optional: Add more detailed logging for debugging
            if (logger.isDebugEnabled()) {
                logger.debug("Collection elements: {}", (Object) collection.getElements());
            }
        } else {
            logger.warn("Collection change detected but collection is null");
        }
    }
}