package com.acrel.entity.base;

import com.acrel.entity.auth.User;
import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.github.pagehelper.util.StringUtil;
import io.jsonwebtoken.*;
import lombok.Data;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
@Data
public class Token {

    public final static String REQUEST_ATTRIBUTE_TOKEN = "token";
    public final static String REQUEST_ATTRIBUTE_USER = "user";

    private static String SECRET_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    private static Long DURATION_MINUTE = 30L;
    
    private User user;
    private Long expiration;
    private boolean persistent;

    /**
     * 判断Token是否过期
     * @return
     */
    public boolean isExpired() {
        if (this.expiration == null) {
            return false;
        }
        Date dateLimit = new Date(this.expiration * 1000L);
        if (dateLimit.before(new Date())) {
            return true;
        }
        return false;
    }

    /**
     * 解析Token
     *
     * @return
     */
    public static Token parse(String tokenString) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(tokenString).getBody();
        String[] strArray = claims.toString()
                .replace(" ", "")
                .replace("{", "")
                .replace("}", "")
                .split(",");
        if (strArray == null || strArray.length <= 0) {
            return null;
        }
        Token token = new Token();
        User user = new User();
        for (String str : strArray) {
            String[] kv = str.split("=");
            if ("id".equals(kv[0])) {
                user.setId(Integer.parseInt(kv[1]));
            }
            if ("account".equals(kv[0])) {
                user.setAccount(kv[1]);
            }
            if ("name".equals(kv[0])) {
                user.setName(kv[1]);
            }
            if ("password".equals(kv[0])) {
                user.setPassword(kv[1]);
                token.setPersistent(true);
            }
            if ("exp".equals(kv[0])) {
                token.setExpiration(Long.parseLong(kv[1]));
            }
        }
        token.setUser(user);
        return token;
    }

    /**
     * 创建Token
     *
     * @return
     */
    public static String generate(User user) {
        return generate(user, DURATION_MINUTE);
    }

    /**
     * 创建Token
     *
     * @return
     */
    public static String generate(User user, Long duration) {
        if (duration == null || duration == 0) {
            duration = DURATION_MINUTE;
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .claim("id", user.getId())
                .claim("account", user.getAccount())
                .claim("name", user.getName())
                .signWith(signatureAlgorithm, signingKey);
        // 添加Token过期时间
        long tokenDurationMilli = duration * 60 * 1000;
        Date expireDate = new Date(nowMillis + tokenDurationMilli);
        builder.setExpiration(expireDate).setNotBefore(now);
        // 生成TOKEN
        return builder.compact();
    }

    /**
     * 生成持久化Token，用户修改密码后失效
     *
     * @return
     */
    public static String persistent(User user) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .claim("id", user.getId())
                .claim("account", user.getAccount())
                .claim("name", user.getName())
                .claim("password", user.getPassword())
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * 校验token是否失效
     * @param user
     * @return
     */
    public static boolean validate(Token token, User user) throws CommonException {
        if (user == null || token == null) {
            throw new CommonException(ExceptionEnum.TOKEN_ERROR);
        }
        User tokenUser = token.getUser();
        if (!user.getAccount().equals(tokenUser.getAccount()) ||
                !user.getName().equals(tokenUser.getName())) {
            throw new CommonException(ExceptionEnum.TOKEN_ERROR);
        }

        // 对比持久化Token的password，判断是否失效
        String password = tokenUser.getPassword();
        if (StringUtil.isNotEmpty(password) && !password.equals(user.getPassword())) {
            throw new CommonException(ExceptionEnum.TOKEN_ERROR);
        }
        return true;
    }

    private void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }


    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setAccount("admin");
        user.setName("系统管理员");
        // 123
        user.setPassword("82ug05b311fs6kb56afa3vqsbr5tnfnf");
        System.out.println(Token.persistent(user));

        // eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJpZCI6IjEiLCJhY2NvdW50IjoiYWRtaW4iLCJuYW1lIjoi57O757uf566h55CG5ZGYIiwicGFzc3dvcmQiOiI4MnVnMDViMzExZnM2a2I1NmFmYTN2cXNicjV0bmZuZiJ9.4VzQzhFPOhTni29-sYqhORSerq5eUcqe1SRhs8u7l3A
    }
}
