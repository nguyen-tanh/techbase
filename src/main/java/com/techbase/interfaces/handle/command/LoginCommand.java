package com.techbase.interfaces.handle.command;

import lombok.Data;

/**
 * @author nguyentanh
 */
@Data
public class LoginCommand {

    private String userName;
    private String password;
}
