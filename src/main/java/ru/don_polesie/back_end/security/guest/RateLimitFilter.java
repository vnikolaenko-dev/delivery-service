package ru.don_polesie.back_end.security.guest;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(HttpServletRequest req) {
        String key = req.getRemoteAddr() + ":" + req.getRequestURI() + ":" + req.getMethod();
        return buckets.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(limit -> limit.capacity(60).refillGreedy(60, Duration.ofMinutes(1)))
                .build());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (resolveBucket(req).tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(429);
            res.getWriter().write("{\"error\":\"Too Many Requests\"}");
        }
    }
}

