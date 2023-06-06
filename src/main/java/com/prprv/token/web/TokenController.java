package com.prprv.token.web;

import com.prprv.token.common.R;
import com.prprv.token.common.security.TokenProvider;
import com.prprv.token.common.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yoooum
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TokenController {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    record Token(String accessToken, String refreshToken) {
    }

    private Token createToken(String username) {
        String accessToken = tokenProvider.createAccessToken(username, null);
        String refreshToken = tokenProvider.createRefreshToken(username);
        return new Token(accessToken, refreshToken);
    }

    /**
     * 登录
     *
     * @param username 用户
     * @param password 密码
     * @return 访问令牌和刷新令牌
     */
    @Operation(summary = "登录，获取令牌")
    @PostMapping("/token")
    public R<Token> login(String username, String password) {
        try {
            UserDetails details = userDetailsService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, details.getPassword()))
                return R.ok(createToken(details.getUsername()));
        } catch (UsernameNotFoundException e) {
            return R.of(401, "该用户未注册");
        }
        return R.of(401, "用户名或密码错误");
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    @Operation(summary = "续期，刷新令牌")
    @GetMapping("/token")
    public Object refresh(String refreshToken) {
        // 1. 判断令牌是否有效
        Claims claims = tokenProvider.parse(refreshToken);
        // 2. 查找用户
        String subject = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        // 3. 生成令牌
        return R.ok(createToken(userDetails.getUsername()));
    }

    /**
     * 用户信息
     */
    @Operation(summary = "用户信息")
    @GetMapping("/user")
    public R<Object> user(@AuthenticationPrincipal User user) {
        return R.ok(user);
    }

    /**
     * 权限测试
     */
    @Operation(summary = "权限测试，已拥有的权限")
    @PreAuthorize("hasAuthority('test:read')")
    @GetMapping("/test1")
    public R<Object> test1() {
        return R.ok("test:read");
    }

    @Operation(summary = "权限测试，未拥有的权限")
    @PreAuthorize("hasAuthority('test:reboot')")
    @GetMapping("/test2")
    public R<Object> test2() {
        return R.ok("test:reboot");
    }
}
