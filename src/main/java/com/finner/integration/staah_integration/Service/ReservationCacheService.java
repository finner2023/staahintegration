package com.finner.integration.staah_integration.Service;

import com.finner.integration.staah_integration.Model.StaahReservation;
import com.finner.integration.staah_integration.util.HashUtils;
import org.springframework.stereotype.Service;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ReservationCacheService {

    private final Cache<String, String> processedReservationsCache;

    public ReservationCacheService() {
        this.processedReservationsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(65, TimeUnit.MINUTES) // STAAH keeps reservation data for 60 mins
                .maximumSize(10_000) // Optional: safe upper bound
                .build();
    }

    public boolean isAlreadyProcessed(String reservationId) {
        return processedReservationsCache.getIfPresent(reservationId) != null;
    }

    public void markAsProcessed(String reservationId, StaahReservation reservation) {
        String hash= HashUtils.computeHash(reservation);
        processedReservationsCache.put(reservationId, hash);
    }
}