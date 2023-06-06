package com.prprv.token.common.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Yoooum
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息和权限并返回一个UserDetails的实现，为了简单，这里使用预设一个固定用户
        // ... 查询操作
        if (username.equals("admin")) return User.withUsername("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("test:read", "test:write")
                .build();
        throw new UsernameNotFoundException("用户不存在");
    }
}
