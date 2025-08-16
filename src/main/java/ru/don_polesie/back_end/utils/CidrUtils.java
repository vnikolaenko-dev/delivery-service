package ru.don_polesie.back_end.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CidrUtils {
    private final byte[] networkBytes;
    private final int prefixLength;

    public CidrUtils(String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid CIDR: " + cidr);
        InetAddress network = InetAddress.getByName(parts[0]);
        this.networkBytes = network.getAddress();
        this.prefixLength = Integer.parseInt(parts[1]);
    }

    public boolean contains(String ipStr) {
        try {
            InetAddress address = InetAddress.getByName(ipStr);
            byte[] addrBytes = address.getAddress();
            if (addrBytes.length != networkBytes.length) return false; // IPv4 vs IPv6 mismatch

            int fullBytes = prefixLength / 8;
            int remainingBits = prefixLength % 8;

            // проверка полных байтов
            for (int i = 0; i < fullBytes; i++) {
                if (addrBytes[i] != networkBytes[i]) return false;
            }

            if (remainingBits > 0) {
                int mask = (~0) << (8 - remainingBits);
                if ((addrBytes[fullBytes] & mask) != (networkBytes[fullBytes] & mask)) return false;
            }

            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}

