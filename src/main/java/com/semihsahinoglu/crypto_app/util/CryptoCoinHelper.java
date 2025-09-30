package com.semihsahinoglu.crypto_app.util;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
public class CryptoCoinHelper {

    public static BigDecimal toBigDecimal(Object value) {
        return switch (value) {
            case null -> BigDecimal.ZERO;
            case String string -> new BigDecimal(string);
            case Number number -> BigDecimal.valueOf(number.doubleValue());
            default -> throw new IllegalArgumentException("Desteklenmeyen Tip: " + value.getClass());
        };
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        return switch (value) {
            case null -> null;
            case Long timestamp -> Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            case Number timestamp -> Instant.ofEpochMilli(timestamp.longValue())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            default -> throw new IllegalArgumentException("Desteklenmeyen Tip: " + value.getClass());
        };
    }
}
