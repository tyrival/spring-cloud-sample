package com.acrel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public class TokenUtils {

    public static String BASE64_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    private static Integer TOKEN_DURATION = 30;

    /**
     * 解析Token
     * @param token
     * @return
     */
    public static Map parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(BASE64_KEY))
                    .parseClaimsJws(token).getBody();
            String[] strArray = claims.toString()
                    .replace(" ", "")
                    .replace("{", "")
                    .replace("}", "")
                    .split(",");
            if (strArray == null || strArray.length <= 0) {
                return null;
            }
            Map map = new HashMap();
            for (String str : strArray) {
                String[] kv = str.split("=");
                map.put(kv[0], kv[1]);
            }
            return map;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建Token
     * @param account
     * @param userId
     * @return
     */
    public static String create(String account, Integer userId) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BASE64_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .claim("account", account)
                .claim("userId", userId)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        long tokenDurationMilli = TOKEN_DURATION * 60 * 1000;
        Date expireDate = new Date(nowMillis + tokenDurationMilli);
        builder.setExpiration(expireDate).setNotBefore(now);
        //生成TOKEN
        return builder.compact();
    }

    /**
     * 创建Token
     * @param account
     * @param password
     * @return
     */
    public static String createAccountToken(String account, String password) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BASE64_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .claim("account", account)
                .claim("password", password)
                .signWith(signatureAlgorithm, signingKey);
        //生成TOKEN
        return builder.compact();
    }
}
