package ru.don_polesie.back_end.security.guest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.repository.OrderRepository;

import java.util.EnumSet;

@Component
public class OrderAccess {

    private final OrderRepository repo;

    public OrderAccess(OrderRepository repo) {
        this.repo = repo;
    }

    public boolean canModify(Long orderId, Authentication auth) {
        if (auth == null) return false;

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        if (auth.getPrincipal() instanceof GuestPrincipal gp) {
            String guestId = gp.getGuestId();
            return repo.existsByIdAndGuestId(
                    orderId, guestId);
        }
        return false;
    }
}
