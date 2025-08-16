package ru.don_polesie.back_end.security.guest;

import java.security.Principal;
import java.util.Objects;

public final class GuestPrincipal implements Principal {
    private final String guestId;

    public GuestPrincipal(String guestId) {
        this.guestId = Objects.requireNonNull(guestId);
    }

    public String getGuestId() {
        return guestId;
    }

    @Override
    public String getName() { // по контракту Principal
        return guestId;
    }

    @Override
    public String toString() {
        return "GuestPrincipal{guestId='" + guestId + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestPrincipal)) return false;
        GuestPrincipal that = (GuestPrincipal) o;
        return guestId.equals(that.guestId);
    }

    @Override
    public int hashCode() {
        return guestId.hashCode();
    }
}

