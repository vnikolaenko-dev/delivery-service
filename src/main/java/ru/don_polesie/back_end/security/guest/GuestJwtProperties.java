package ru.don_polesie.back_end.security.guest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "security.guest")
public class GuestJwtProperties {
    private String secret;
    private long ttl;
    private String issuer;
}

