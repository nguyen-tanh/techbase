package com.techbase.interfaces.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nguyentanh
 */
@RequestMapping("/api/")
public abstract class AbstractAPIController {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected MessageSource messageSource() {
        return messageSource;
    }
}
