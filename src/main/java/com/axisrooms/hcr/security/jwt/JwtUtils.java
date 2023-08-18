package com.axisrooms.hcr.security.jwt;

import com.axisrooms.hcr.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${hcr.app.jwtSecret}")
  private String jwtSecret;

  @Value("${hcr.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${hcr.app.jwtCookieName}")
  private String jwtCookie;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    return cookie;
  }
  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String jwt,HttpServletRequest request) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
      return true;
    } catch (SignatureException e) {
      request.setAttribute("message","Invalid JWT signature: "+e.getMessage());
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      request.setAttribute("message","Invalid JWT token: "+e.getMessage());
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      request.setAttribute("message","JWT token is expired: "+e.getMessage());
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      request.setAttribute("message","JWT token is unsupported: "+e.getMessage());
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      request.setAttribute("message","JWT claims string is empty: "+e.getMessage());
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public String validateJwtToken(String jwt) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
      return "success";
    } catch (SignatureException e) {
      return "Invalid JWT signature: "+e.getMessage();
    } catch (MalformedJwtException e) {
      return "Invalid JWT token: "+e.getMessage();
    } catch (ExpiredJwtException e) {
      return "JWT token is expired: "+e.getMessage();
    } catch (UnsupportedJwtException e) {
      return "JWT token is unsupported: "+e.getMessage();
    } catch (IllegalArgumentException e) {
      return "JWT claims string is empty: "+e.getMessage();
    }
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
}
