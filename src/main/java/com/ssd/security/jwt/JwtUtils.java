package com.ssd.security.jwt;

import java.util.Date;

import com.ssd.dal.model.UserDetailsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //Get access JWT Secret in the property file
    @Value("${com.ssd.jwtSecret}")
    private String jwtSecretKey;

    //Get access JWT Expiration time in the property file
    @Value("${com.ssd.jwtExpirationMs}")
    private int jwtExpirationMs;

    //Generate JWT Token method
    public String generateJwtToken(Authentication authentication) {

        UserDetailsModel userPrincipal = (UserDetailsModel) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //Check and Validate the JWT Token
    public boolean validateJwtToken(String authToken) {

        try {

            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
            return true;

        } catch (SignatureException e) {

            logger.error("Invalid JWT signature: {}", e.getMessage());

        } catch (MalformedJwtException e) {

            logger.error("Invalid JWT token: {}", e.getMessage());

        } catch (ExpiredJwtException e) {

            logger.error("JWT token is expired: {}", e.getMessage());

        } catch (UnsupportedJwtException e) {

            logger.error("JWT token is unsupported: {}", e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("JWT claims string is empty: {}", e.getMessage());

        }
        return false;
    }
}
