package com.busmonk.util;



//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;

/**
 * Created by sr250345 on 11/21/16.
 */

public class JWTUtil {

    public static String SIGNING_KEY = "abcde0123456789";

    public static String getUserIDFromJWT(String jwt)
    {
        //Claims claims = Jwts.parser().setSigningKey(Base64.decode(SIGNING_KEY, Base64.DEFAULT)).parseClaimsJws(jwt).getBody();
        //return claims.getSubject();
        return "12345";
    }
}
