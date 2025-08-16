package ru.don_polesie.back_end.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.don_polesie.back_end.security.admin.JwtTokenFilter;
import ru.don_polesie.back_end.security.admin.JwtTokenProvider;
import ru.don_polesie.back_end.security.guest.GuestTokenFilter;
import ru.don_polesie.back_end.security.guest.GuestTokenProvider;
import ru.don_polesie.back_end.security.guest.RateLimitFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final RateLimitFilter rateLimitFilter;
    private final GuestTokenProvider guestTokenProvider;

    @Bean
    public GuestTokenFilter guestTokenFilter() {
        return new GuestTokenFilter(guestTokenProvider);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(tokenProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // здесь укажите адрес вашего фронтенда
        cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3002"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // применяем ко всем URL
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(guestTokenFilter(), UsernamePasswordAuthenticationFilter.class)// затем гость (Header: Guest ...)
                .addFilterBefore(jwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class) // потом админ Bearer
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("{\"error\": \"Access Denied\"}");
                        })
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeHttpRequests(auth -> auth
                        // preflight, если надо
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // админ-аутентификация
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/login").permitAll()
                        // (если есть refresh)
                        .requestMatchers(HttpMethod.POST, "/api/admin/auth/refresh").permitAll()

                        // весь админский API
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // публичный каталог
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // создание заказа — публично (гостевой токен выдаём в ответе)
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").permitAll()

                        // чтение/изменение/удаление заказа — только владелец (ROLE_GUEST) или админ
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("GUEST","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyRole("GUEST","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasAnyRole("GUEST","ADMIN")

                        // платежи: инициировать оплату — гость/админ,
                        .requestMatchers("/api/payment/**").hasAnyRole("GUEST","ADMIN")
                        // а вебхук от платёжки (если есть) — отдельный путь и проверка подписи:
                        // .requestMatchers(HttpMethod.POST, "/api/payment/webhook").permitAll()

                        .anyRequest().denyAll()
                );

        return httpSecurity.build();
    }

}