package njp.raf.cloud.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import njp.raf.cloud.exception.user.TokenParsingException;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.dto.TokenResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@PropertySource(value = "/security.properties")
public class TokenService {

    private final String tokenSecret;

    public TokenService(@Value("${token.secret}") String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public TokenResponse generateTokenFor(User user) {
        String token = Jwts.builder()
                .setClaims(createClaims(user))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        return new TokenResponse(token);
    }

    private Claims createClaims(User user) {
        Claims claims = Jwts.claims();

        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        return claims;
    }

    public Claims parseToken(ProceedingJoinPoint joinPoint) {
        String token = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .filter(headerName -> headerName.startsWith("Bearer"))
                .findFirst()
                .orElseThrow(() -> new TokenParsingException("Bearer header not found!"))
                .split(" ")[1];

        return parseToken(token);
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ignored) {
            throw new TokenParsingException(String.format("Invalid token: %s!", token));
        }
    }

}
