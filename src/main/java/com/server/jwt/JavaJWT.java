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


    public static String createToken(String id, List<String> authList) {
        return createToken(id, authList, 8);
    }


    public static String createToken(String id, List<String> authList, int expiresDayFromNow) {
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
            builder.withArrayClaim("authList", authList.toArray(new String[]{}));
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
            // private static final User dao = new User().dao();
            // User user = new User().dao().findById(JWT.decode(token).getClaim("id").asInt());
            // addClaimByUser(builder, user);
            builder.withClaim("id", getId(token));
            builder.withArrayClaim("authList", getAuthList("authList").toArray(new String[]{}));
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

    public static String getId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asString();
    }

    public static List<String> getAuthList(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("authList").asList(String.class);
    }

    public static String getClaim(String token, String claimKey) {
//        try {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(claimKey).asString();
//        }catch(Exception e){
//            return "guest";
//        }
    }
}
