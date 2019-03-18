package com.github.missthee.jwt;

public interface UserInfoForJWT {
    /**
     * @param obj
     * @return
     * @apiNote A function of getting user's password. JWT need the value used as secret.
     */
    String getSecret(Object obj);



}
