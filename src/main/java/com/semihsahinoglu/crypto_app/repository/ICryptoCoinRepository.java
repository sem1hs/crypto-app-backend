package com.semihsahinoglu.crypto_app.repository;

import com.semihsahinoglu.crypto_app.entity.CryptoCoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ICryptoCoinRepository extends JpaRepository<CryptoCoin, Long> {
    boolean existsBySymbolAndOpenTime(String symbol, LocalDateTime openTime);

    Page<CryptoCoin> findAllBySymbol(String symbol, Pageable pageable);

    void deleteAllBySymbol(String symbol);
}
