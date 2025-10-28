package ru.don_polesie.back_end.security.admin;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.exceptions.AccessDeniedException;
import ru.don_polesie.back_end.model.Role;
import ru.don_polesie.back_end.service.staffOnly.StaffService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService jwtUserDetailsService;
    private final StaffService userServiceImpl;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String phoneNumber, Set<Role> roles) {
        var now = new Date();
        var exp = new Date(now.getTime() + jwtProperties.getAccess());

        var claims = Jwts.claims()
                .subject(phoneNumber)
                .issuer(jwtProperties.getIssuer())
                .add("id", userId)
                .add("roles", resolveRoles(roles))
                .add("typ", "access")
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String phoneNumber) {
        var now = new Date();
        var exp = new Date(now.getTime() + jwtProperties.getRefresh());

        var claims = Jwts.claims()
                .subject(phoneNumber)
                .issuer(jwtProperties.getIssuer())
                .add("id", userId)
                .add("typ", "refresh")
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public JwtAuthResponse refreshUserToken(String refreshToken) {
        var jwtResponse = new JwtAuthResponse();

        if (!validateToken(refreshToken, "refresh")) {
            throw new AccessDeniedException("Invalid refresh token");
        }

        var userId = Long.valueOf(getId(refreshToken));
        var user = userServiceImpl.getById(userId);

        jwtResponse.setId(userId);
        jwtResponse.setPhoneNumber(user.getPhoneNumber());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getPhoneNumber(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getPhoneNumber()));
        return jwtResponse;
    }

    /** Обобщённая валидация: срок + (опц.) issuer + (опц.) ожидаемый тип токена. */
    public boolean validateToken(String token, String expectedTyp) {
        try {
            Jws<Claims> jws = Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            var c = jws.getPayload();

            if (c.getExpiration().before(new Date())) return false;

            var iss = jwtProperties.getIssuer();
            if (iss != null && !iss.equals(c.getIssuer())) return false;

            if (expectedTyp != null) {
                Object typ = c.get("typ");
                if (!expectedTyp.equals(typ)) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Старый метод оставим для совместимости, без проверки типа. */
    public boolean validateToken(String token) {
        return validateToken(token, null);
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    private String getId(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token).getPayload().get("id").toString();
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public Authentication getAuthentication(String token) {
        var username = getUsername(token);
        var userDetails = jwtUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
