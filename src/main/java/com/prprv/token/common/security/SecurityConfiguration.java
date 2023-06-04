package com.prprv.token.common.security;

import com.prprv.token.common.security.handler.AccessDeniedHandlerImpl;
import com.prprv.token.common.security.handler.AuthenticationEntryPointImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 前后端分离，不需要csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 跨域配置
                .cors(conf -> conf.configurationSource(cors()))
                // 请求认证
                .authorizeHttpRequests(conf -> {
                    // 不需要认证的请求
                    conf.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/doc.html",
                            "/swagger-resources/**",
                            "/v3/api-docs/**",
                            "/webjars/**",
                            "/auth/token"
                    ).permitAll();
                    // 其他所有请求需要认证
                    conf.anyRequest().authenticated();
                })

                // 异常处理
                .exceptionHandling(conf -> {
                    // 403，权限不足
                    conf.accessDeniedHandler(accessDeniedHandler);
                    // 401，认证失败
                    conf.authenticationEntryPoint(authenticationEntryPoint);
                })
                // 使用jwt认证，不需要session
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加jwt过滤器
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource cors() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
