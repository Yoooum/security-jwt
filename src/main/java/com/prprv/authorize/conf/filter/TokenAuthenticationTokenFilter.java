package com.prprv.authorize.conf.filter;

import com.prprv.authorize.conf.TokenProvider;
import com.prprv.authorize.conf.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Yoooum
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerToken(request);
        if (token != null) {
            Claims claims = tokenProvider.parse(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // 获取 request.getRemoteAddr() 等信息 到 authenticationToken 中
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 更新上下文中的认证信息，将身份认证令牌写入上下文，
            // 这样就可以在 Controller 中使用 @AuthenticationPrincipal 获取当前用户信息。
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // 放行请求，让后面的过滤器去报错
        filterChain.doFilter(request, response);
    }

    /**
     * JWT 属于 BearerToken，在请求头中格式为：Authorization: Bearer <token>
     */
    private String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
