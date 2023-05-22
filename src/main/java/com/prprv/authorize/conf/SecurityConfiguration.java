package com.prprv.authorize.conf;

import com.prprv.authorize.conf.filter.TokenAuthenticationTokenFilter;
import com.prprv.authorize.conf.handle.AccessDeniedHandlerImpl;
import com.prprv.authorize.conf.handle.AuthenticationEntryPointImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Yoooum
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccessDeniedHandlerImpl accessDeniedHandler;

    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    private final TokenAuthenticationTokenFilter tokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                // 请求认证
                .authorizeHttpRequests(conf -> {
                    conf.requestMatchers("/login").permitAll();
                    conf.anyRequest().authenticated();
                })

                // 异常处理
                .exceptionHandling(conf -> {
                    conf.accessDeniedHandler(accessDeniedHandler);
                    conf.authenticationEntryPoint(authenticationEntryPoint);
                })
                // 使用token，不需要session
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加Token过滤器
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
