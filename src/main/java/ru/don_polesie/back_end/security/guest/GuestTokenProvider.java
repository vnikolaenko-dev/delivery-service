package ru.don_polesie.back_end.security.guest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class GuestTokenProvider {

    private final GuestJwtProperties props;
    private SecretKey key;

    public GuestTokenProvider(GuestJwtProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        // HS256 требует секрет ≥ 256 бит
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /** Генерируем токен для guestId (обычно UUID). */
    public String createToken(String guestId) {
        var now = new Date();
        var exp = new Date(now.getTime() + props.getTtl());

        var claims = Jwts.claims()
                .subject(guestId)
                .add("typ", "guest")
                .build();

        return Jwts.builder()
                .issuer(props.getIssuer())
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /** Проверка подписи, срока и типа токена. */
    public boolean validate(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            var payload = jws.getPayload();
            if (payload.getExpiration().before(new Date())) return false;
            Object typ = payload.get("typ");
            return "guest".equals(typ);
        } catch (Exception e) {
            return false;
        }
    }

    /** Достаём guestId (из subject). */
    public String getGuestId(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
