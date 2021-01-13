package com.techbase.interfaces.handle.response;

import lombok.Getter;

/**
 * @author nguyentanh
 */
@Getter
public class ErrorMessage {

    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
