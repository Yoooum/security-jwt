package com.prprv.token.common.security;

import com.prprv.token.common.R;
import com.prprv.token.exception.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Yoooum
 */
@Slf4j
@Component
public class TokenProvider {
    // 密钥
    @Value("${token.secret}")
    private String SECRET_KEY;

    // 访问令牌的有效时间
    @Value("${token.ttl.access-token}")
    private Integer ACCESS_TTL;

    // 刷新令牌的有效时间
    @Value("${token.ttl.refresh-token}")
    private Integer REFRESH_TTL;

    public Integer getExpiresIn() {
        return ACCESS_TTL;
    }

    /**
     * 返回一个密钥实例，基于 HMAC-SHA 算法
     */
    private Key secretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成访问令牌
     *
     * @param subject 主题，通常是用户名或用户ID
     * @param claims  用户信息
     * @return 访问令牌
     */
    public String createAccessToken(String subject, Map<String, Object> claims) {
        return createToken(subject, claims, ACCESS_TTL);
    }

    /**
     * 生成刷新令牌
     *
     * @param subject 主题，通常是用户名或用户ID
     * @return 刷新令牌
     */
    public String createRefreshToken(String subject) {
        return createToken(subject, null, REFRESH_TTL);
    }

    public String createToken(String subject, Map<String, Object> claims, Integer ttl) {
        try {
            JwtBuilder builder = Jwts.builder()
                    // 主题，通常是用户名或用户ID
                    .setSubject(subject)
                    // 指定标头
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    // JWT 唯一标识
                    .setId(UUID.randomUUID().toString())
                    // 签发时间
                    .setIssuedAt(new Date())
                    // 过期时间
                    .setExpiration(new Date(System.currentTimeMillis() + ttl * 1000))
                    // 签名指定算法
                    .signWith(secretKey(), SignatureAlgorithm.HS256);
            // 有效载荷，存放用户脱敏信息，通常是用户权限
            if (claims != null) builder.setClaims(claims);
            return builder.compact();
        } catch (Exception e) {
            log.error("Token create error", e);
            throw new AppException(e);
        }

    }

    /**
     * 解析令牌，判断是否有效
     *
     * @param token JWT令牌
     * @return JWT声明集
     */
    public Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired", e);
            throw new AppException(R.of(401,"令牌已过期，请重新登录"));
        } catch (Exception e) {
            log.error("Token parse error", e);
            throw new AppException(e);
        }
    }
}
