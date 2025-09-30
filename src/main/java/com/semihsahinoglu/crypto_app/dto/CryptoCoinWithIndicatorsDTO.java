package com.semihsahinoglu.crypto_app.dto;

import com.semihsahinoglu.crypto_app.entity.CryptoCoin;

import java.util.List;
import java.util.Map;

public record CryptoCoinWithIndicatorsDTO(
        List<CryptoCoin> cryptoCoins,
        Map<String, Object> indicators
) {
}
