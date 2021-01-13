package com.techbase.support.exception;

/**
 * @author nguyentanh
 */
public class NotFoundTokenAuthentication extends RuntimeException {

    public NotFoundTokenAuthentication(String message) {
        super(message);
    }
}
