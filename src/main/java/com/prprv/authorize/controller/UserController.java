package com.prprv.authorize.controller;

import com.prprv.authorize.conf.TokenProvider;
import com.prprv.authorize.conf.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yoooum
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final TokenProvider tokenProvider;

    private final UserDetailsServiceImpl userDetailsService;

    record LoginValue(String username, String password) {
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginValue loginValue) {
        // 查找用户
        UserDetails details = userDetailsService.loadUserByUsername(loginValue.username());
        // 返回令牌
        String accessToken = tokenProvider.createAccessToken(details.getUsername(), new HashMap<>());
        return Map.of("Authorization", "Bearer " + accessToken);
    }

    @PreAuthorize("hasAuthority('test:read')")
    @GetMapping("/read")
    public String read() {
        return "read";
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/default")
    public String permit() {
        return "default";
    }
}
