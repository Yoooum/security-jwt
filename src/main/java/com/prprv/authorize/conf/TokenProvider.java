package com.prprv.authorize.conf;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    @Value("${token.ttl.access}")
    private Long ACCESS_TTL;

    // 刷新令牌的有效时间
    @Value("${token.ttl.refresh}")
    private Long REFRESH_TTL;

    /**
     * 密钥实例
     */
    private Key secretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成访问令牌
     * @param subject 主题，通常是用户名或用户ID
     * @param claims 用户信息
     * @return 访问令牌
     */
    public String createAccessToken(String subject, Map<String, Object> claims) {
        return createToken(subject, claims, ACCESS_TTL);
    }

    /**
     * 生成刷新令牌
     * @param subject 主题，通常是用户名或用户ID
     * @param claims 用户信息
     * @return 刷新令牌
     */
    public String createRefreshToken(String subject, Map<String, Object> claims) {
        return createToken(subject, claims, REFRESH_TTL);
    }
    
    public String createToken(String subject, Map<String, Object> claims, Long ttl) {
        try {
            return Jwts.builder()
                    // 主题，通常是用户名或用户ID
                    .setSubject(subject)
                    // 有效载荷，存放用户脱敏信息
                    .setClaims(claims)
                    // 指定标头
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    // JWT 唯一标识
                    .setId(UUID.randomUUID().toString())
                    // 签发时间
                    .setIssuedAt(new Date())
                    // 过期时间
                    .setExpiration(new Date(System.currentTimeMillis() + ttl * 1000))
                    // 签名指定算法
                    .signWith(secretKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Token create error", e);
            throw new RuntimeException(e);
        }

    }

    public Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Token parse error", e);
            throw new RuntimeException(e);
        }
    }
}
