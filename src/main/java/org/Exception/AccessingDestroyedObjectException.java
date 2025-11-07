package org.Exception;

public class AccessingDestroyedObjectException extends RuntimeException {
    public AccessingDestroyedObjectException(String message) {
        super(message);
    }
}
