package com.techbase.interfaces.shared;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.techbase.interfaces.handle.response.APIResponse;
import com.techbase.interfaces.handle.response.ErrorMessage;
import com.techbase.support.exception.NotFoundTokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author nguyentanh
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundTokenAuthentication.class)
    @ResponseBody
    public APIResponse<ErrorMessage> handleException(NotFoundTokenAuthentication e) {
        log.error(e.getMessage(), e);

        String message = messageSource.getMessage("authentication.jwt.not-found", new Object[0], LocaleContextHolder.getLocale());
        APIResponse<ErrorMessage> response = new APIResponse<>();

        response.statusCode(HttpStatus.UNAUTHORIZED);
        response.statusMessage(APIResponse.StatusMessage.HEADER_ERROR);
        response.addError(new ErrorMessage(message));

        return response;
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseBody
    public APIResponse<ErrorMessage> handleException(JWTVerificationException e) {
        log.error(e.getMessage(), e);

        String message = messageSource.getMessage("authentication.jwt.invalid", new Object[0], LocaleContextHolder.getLocale());
        APIResponse<ErrorMessage> response = new APIResponse<>();

        response.statusCode(HttpStatus.UNAUTHORIZED);
        response.statusMessage(APIResponse.StatusMessage.AUTHENTICATION_ERROR);
        response.addError(new ErrorMessage(message));

        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public APIResponse<ErrorMessage> handleException(AccessDeniedException e) {
        log.error(e.getMessage(), e);

        String message = messageSource.getMessage("authentication.access.denied", new Object[0], LocaleContextHolder.getLocale());
        APIResponse<ErrorMessage> response = new APIResponse<>();

        response.statusCode(HttpStatus.FORBIDDEN);
        response.statusMessage(APIResponse.StatusMessage.AUTHENTICATION_ERROR);
        response.addError(new ErrorMessage(message));

        return response;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public APIResponse<ErrorMessage> handleException(Exception e) {
        log.error(e.getMessage(), e);

        String message = messageSource.getMessage("msg.request.internal-error", new Object[0], LocaleContextHolder.getLocale());
        APIResponse<ErrorMessage> response = new APIResponse<>();

        response.statusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.statusMessage(APIResponse.StatusMessage.SERVER_ERROR);
        response.addError(new ErrorMessage(message));

        return response;
    }

}
