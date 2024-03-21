package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationServiceProxy service;


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest servletRequest
    ) {
        ProxyFactory factory = new ProxyFactory(service);
        factory.addInterface(AuthenticationService.class);
        AuthenticationService authenticationServiceProxy = (AuthenticationService) factory.getProxy();
        authenticationServiceProxy.register(request, getSiteURL(servletRequest), Role.USER);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        ProxyFactory factory = new ProxyFactory(service);
        factory.addInterface(AuthenticationService.class);
        AuthenticationService authenticationServiceProxy = (AuthenticationService) factory.getProxy();
        return ResponseEntity.ok(authenticationServiceProxy.authenticate(request, response));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyUser(@Param("code") String code) {
        ProxyFactory factory = new ProxyFactory(service);
        factory.addInterface(AuthenticationService.class);
        AuthenticationService authenticationServiceProxy = (AuthenticationService) factory.getProxy();
        authenticationServiceProxy.verify(code);
        return ResponseEntity.ok().build();
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
