package com.techbase.interfaces.authentication;

import com.techbase.application.AuthenticationService;
import com.techbase.domain.Employee;
import com.techbase.domain.EmployeeRepository;
import com.techbase.interfaces.handle.command.LoginCommand;
import com.techbase.interfaces.handle.dto.TokenDTO;
import com.techbase.interfaces.handle.response.APIResponse;
import com.techbase.interfaces.handle.response.ErrorMessage;
import com.techbase.interfaces.shared.AbstractAPIController;
import configuration.web.JWTConfig;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author nguyentanh
 */
@RestController
public class AuthenticationController extends AbstractAPIController {

    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final JWTConfig.JWTExpired jwtExpired;

    public AuthenticationController(EmployeeRepository employeeRepository,
                                    AuthenticationService authenticationService,
                                    PasswordEncoder passwordEncoder,
                                    JWTConfig.JWTExpired jwtExpired) {
        this.employeeRepository = employeeRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtExpired = jwtExpired;
    }

    @PostMapping("auth")
    @ResponseBody
    public APIResponse<TokenDTO> authenticate(@RequestBody LoginCommand loginCommand) {
        APIResponse<TokenDTO> response = new APIResponse<>();

        Optional<Employee> employeeOptional = employeeRepository.findByUserName(loginCommand.getUserName());

        if (employeeOptional.isPresent() && employeeOptional.get().verifyPassword(passwordEncoder, loginCommand.getPassword())) {
            Employee employee = employeeOptional.get();

            String accessToken = authenticationService.createToken(employee.userName(), employee.role(), jwtExpired.getAccessToken());
            String refreshToken = authenticationService.createToken(employee.userName(), employee.role(), jwtExpired.getRefreshToken());

            response.addData(new TokenDTO(accessToken, refreshToken));
            response.statusMessage(APIResponse.StatusMessage.SUCCESS);

            return response;
        } else {

            response.statusCode(HttpStatus.UNAUTHORIZED);
            response.addError(new ErrorMessage(messageSource().getMessage("authentication.login.fail", new Object[0], LocaleContextHolder.getLocale())));
            response.statusMessage(APIResponse.StatusMessage.AUTHENTICATION_ERROR);
        }

        return response;
    }
}
