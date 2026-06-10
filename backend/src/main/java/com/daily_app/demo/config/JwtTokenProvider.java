package com.daily_app.demo.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        // 文字列の秘密鍵から HMAC 用の鍵を生成
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /** mailAddress からトークンを生成 */
    public String generateToken(String mailAddress) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(mailAddress)          // sub クレーム
                .issuedAt(now)              // iat
                .expiration(expiry)         // exp
                .signWith(key)              // 署名（アルゴリズムは鍵から自動判定）
                .compact();
    }

    /** トークンから mailAddress を取り出す */
    public String getMailAddress(String token) {
        return parseClaims(token).getSubject();
    }

    /** 署名・有効期限を検証。例外が出なければ true */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 署名不正・期限切れ・改ざんなど
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)            // 鍵で署名検証
                .build()
                .parseSignedClaims(token)   // 検証つきパース
                .getPayload();
    }
}