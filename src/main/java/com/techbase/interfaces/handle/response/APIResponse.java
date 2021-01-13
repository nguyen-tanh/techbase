package com.techbase.interfaces.handle.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nguyentanh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "statusCode" })
@Getter
public class APIResponse<T> {

    private HttpStatus statusCode = HttpStatus.OK;

    private String statusMessage;

    private T data;

    private List<ErrorMessage> errorMessages;

    public void statusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public void statusMessage(StatusMessage status) {
        this.statusMessage = status.getStatus();
    }

    public void addData(T t) {
        data = t;
    }

    public void addError(ErrorMessage errorMessage) {
        if (errorMessages == null) errorMessages = new ArrayList<>();

        this.errorMessages.add(errorMessage);
    }

    public enum StatusMessage {
        SUCCESS("000"),
        HEADER_ERROR("200"),
        AUTHENTICATION_ERROR("300"),
        INPUT_ERROR("400"),
        SERVER_ERROR("500");

        private final String status;

        StatusMessage(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

}
