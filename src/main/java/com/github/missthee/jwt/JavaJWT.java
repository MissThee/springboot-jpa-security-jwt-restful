package com.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.server.tool.GetDate;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JavaJWT {
    private static final String issuer = "Demo1";       //发布者
    private static final String secret = "MIL7kBs6Z";   //验证token密钥


    public static String createToken(String id) {
        return createToken(id,8);
    }


    public static String createToken(String id, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //Map<String, Object> headerClaims = new HashMap();
            //headerClaims.put("userId", "1234");
            //builder.withHeader(headerClaims);//为jwt添加header键值对
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(GetDate.now(expiresDayFromNow));
            // addClaimByUser(builder, user);
            builder.withClaim("id", id);
            String token = builder.sign(algorithm);
            log.info("CREATE TOKEN：" + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            // UTF-8 encoding not supported
            return "";
        }
    }


    public static String updateToken(String token, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(GetDate.now(expiresDayFromNow));
            builder.withClaim("id", getId(token));
            String newToken = builder.sign(algorithm);
            log.info("REFRESH TOKEN:" + newToken);
            return newToken;
        } catch (Exception e) {
            // UTF-8 encoding not supported
        }
        return "";
    }

    public static String updateToken(String token) {
        return updateToken(token, 8);
    }

    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            log.info("CHECK TOEKN: Fine");
            return true;
        } catch (Exception e) {
            log.info("CHECK TOEKN-ERROR: " + e);
            return false;
        }

    }

    /**
     * @return int剩余有效时间(天)
     */
    public static int getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return GetDate.dateDiff(GetDate.now(), jwt.getExpiresAt(), 1);
    }

    public static String getId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asString();
    }

}
