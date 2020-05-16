package com.gdou.teaching.util;

import com.gdou.teaching.constant.SecurityConstants;
import com.gdou.teaching.mbg.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author carrymaniac
 *
 */
@Component
@Slf4j
public class JWTUtil {

    private static final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);
    private static SecretKey secretKey = Keys.hmacShaKeyFor(apiKeySecretBytes);
    /**
     * 生成Token
     * @param userNumber
     * @param claims
     * @return
     */
    private String genToken(String userNumber, Map<String,Object> claims){
        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime() + SecurityConstants.EXPIRATION*1000);
        return  Jwts.builder()
                .setSubject(userNumber) //Subject
                .setClaims(claims) //负载
                .setExpiration(expirationDate) //过期时间
                .signWith(secretKey,SignatureAlgorithm.HS256) //签名
                .compact(); }

    public String genToken(User user){
        Map<String,Object> claims = new HashMap<>(2);
        claims.put("CLAIM_KEY_USER_ID",user.getUserId());
        claims.put("CLAIM_KEY_CREATED",new Date());
        return genToken(user.getUserNumber(),claims);
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}",token);
        }
        return claims;
    }
    public Integer getUserIdFromToken(String token){
        Claims claimsFromToken = getClaimsFromToken(token);
        Integer userId = (Integer)claimsFromToken.get("CLAIM_KEY_USER_ID");
        return userId;
    }
    public String getUserNumberFromToken(String token){
        Claims claimsFromToken = getClaimsFromToken(token);
        return  claimsFromToken.getSubject();
    }
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put("CLAIM_KEY_CREATED",new Date());
        return genToken(claims.getSubject(),claims);
    }

    public boolean validateToken(String token, User user){
        String userName = getUserNumberFromToken(token);
        return userName.equals(user.getUserNumber()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date date = getExpiredDateFromToken(token);
        return date.before(new Date());
    }


}
