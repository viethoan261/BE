package hoan.com.springboot.security.jwt;

import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private String jwtSecret = "test";
    private String prefix = "Bearer ";

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private UserRepository userRepository;


    public String generateJwtToken(String username) {

        UserEntity userEntity = userRepository.findByUsername(username).get();

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userEntity.getId().toString());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS512, "test")
                .compact();
    }

    public String getToken(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        if (jwt == null)
            return null;

        return jwt.substring(prefix.length());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getIssuedAt(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuedAt();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        String userId = claims.get("userId", String.class);
        return userId;
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
