package com.tenmax.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tenmax.tool.GetDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class JavaJWT {
    private static final String issuer = "Demo1";       //发布者
    private static final String secret = "MIL7kBs6Z";   //验证token密钥
    private static  Logger logger = LoggerFactory.getLogger(JavaJWT.class);
    /**
     * @param id              用户id
     * @param expiresDayFromNow 有效时间（天）
     */
    public static String createToken(Object id, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //Map<String, Object> headerClaims = new HashMap();
            //headerClaims.put("userId", "1234");
            //builder.withHeader(headerClaims);//为jwt添加header键值对
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(GetDate.now(expiresDayFromNow));
            // addClaimByUser(builder, user);
            builder.withClaim("id", id.toString());
            String token = builder.sign(algorithm);
            logger.info("CREATE TOKEN：" + token);
            return token;
        } catch (UnsupportedEncodingException exception) {
            // UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn‘t convert Claims.
        }
        return "";
    }

    /**
     * @param id 用户id 有效时间（默认8天）
     */
    public static String createToken(Object id) {
        return createToken(id, 8);
    }

    public static String updateToken(String token, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(GetDate.now(expiresDayFromNow));
            // private static final User dao = new User().dao();
            // User user = new User().dao().findById(JWT.decode(token).getClaim("id").asInt());
            // addClaimByUser(builder, user);
            builder.withClaim("id", JWT.decode(token).getClaim("id").asString());

            String newToken = builder.sign(algorithm);
            logger.info("REFRESH TOKEN:" + newToken);
            return newToken;
        } catch (UnsupportedEncodingException exception) {
            // UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn‘t convert Claims.
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
                    .build(); // Reusable
            // instance
            DecodedJWT jwt = verifier.verify(token);
            logger.info("CHECK TOEKN: Fine");
            return true;
        } catch (UnsupportedEncodingException exception) {
            logger.info("CHECK TOEKN-ERROR: " + exception);
            // UTF-8 encoding not supported
        } catch (JWTVerificationException exception) {
            logger.info("CHECK TOEKN-ERROR: " + exception);
            // Invalid signature/claims
        }
        return false;
    }

    /**
     * @return int剩余有效时间(天)
     */
    public static int getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return GetDate.dateDiff(GetDate.now(), jwt.getExpiresAt(), 1);
    }

//    private static void addClaimByUser(JWTCreator.Builder builder, User user) {
//        builder.withClaim("id", user.getId());
//        builder.withClaim("loginId", user.getLoginId());
//        builder.withClaim("name", user.getName());
//        builder.withClaim("lastLoginDate", user.getLastLoginDate());
//        builder.withClaim("isAdmin", user.getIsAdmin());
//    }

    public static void decodeToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            System.out.println("解码token:");
            System.out.println(jwt.getHeader());
            System.out.println(jwt.getPayload());
            System.out.println(jwt.getSignature());
            System.out.println(jwt.getExpiresAt());
            System.out.println(jwt.getClaims());
        } catch (JWTDecodeException exception) {
            // Invalid token
            System.out.println("解码token:Error:" + exception);
        }
    }

    public static String getId (String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asString();
    }

}
