package com.techbase.interfaces.handle.dto;

import lombok.Getter;

/**
 * @author nguyentanh
 */
@Getter
public class TokenDTO {

    private final String accessToken;
    private final String refreshToken;

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
