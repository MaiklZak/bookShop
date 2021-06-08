package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final TokenRevokerRepository tokenRevokerRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public CustomLogoutSuccessHandler(TokenRevokerRepository tokenRevokerRepository, JWTUtil jwtUtil) {
        this.tokenRevokerRepository = tokenRevokerRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                TokenRevoker tokenRevoker = new TokenRevoker(cookie.getValue(),
                        new Date(jwtUtil.extractExpiration(cookie.getValue()).getTime()));
                tokenRevokerRepository.save(tokenRevoker);
            }
        }
        super.onLogoutSuccess(request, response, authentication);
    }
}
