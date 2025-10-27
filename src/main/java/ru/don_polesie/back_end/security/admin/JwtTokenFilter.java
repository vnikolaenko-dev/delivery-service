package ru.don_polesie.back_end.security.admin;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private  final JwtTokenProvider jwtTokenProvider;
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) {
        var bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }

        if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
            try {
                var authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (ObjectNotFoundException ignored) {}
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
