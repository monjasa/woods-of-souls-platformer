package org.monjasa.engine.util;

public class EmptyCircularQueueException extends RuntimeException {

    public EmptyCircularQueueException() {
        super();
    }

    public EmptyCircularQueueException(String message) {
        super(message);
    }
}
