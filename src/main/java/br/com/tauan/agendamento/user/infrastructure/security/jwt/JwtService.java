package br.com.tauan.agendamento.user.infrastructure.security.jwt;

import br.com.tauan.agendamento.user.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;
    private final long expiration;

    public JwtService(JwtProperties props) {
        byte[] keyBytes = Decoders.BASE64.decode(props.secret());

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = props.expiration();
    }

    public String generateToken(String userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
