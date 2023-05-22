package com.prprv.authorize.conf.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yoooum
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;
    private final Map<String, Object> map = new HashMap<>();

    public LogoutSuccessHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        map.put("code", HttpServletResponse.SC_OK);
        map.put("message", "退出成功");
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
