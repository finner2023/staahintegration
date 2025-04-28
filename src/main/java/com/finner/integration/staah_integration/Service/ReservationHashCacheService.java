package com.finner.integration.staah_integration.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finner.integration.staah_integration.Model.StaahReservation;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class ReservationHashCacheService {
    private final Map<String,String> fullResponseHashCache=new ConcurrentHashMap<>();
    private final Map<String ,String> reservationHashCache=new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper=new ObjectMapper();

    public boolean isSameAsLastFullResponse(String hotelId,StaahReservation[] reservations){
        try {
            String currentJson=objectMapper.writeValueAsString(reservations);
            String currentHash=hash(currentJson);
            String previousHash=fullResponseHashCache.get(hotelId);
            boolean same=currentHash.equals(previousHash);
            if(!same) fullResponseHashCache.put(hotelId,currentHash);
            return same;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isSameReservation(String reservationId, StaahReservation reservation){
        try {
            String currentJson=objectMapper.writeValueAsString(reservation);
            String currentHash=hash(currentJson);
            String previousHash=fullResponseHashCache.get(reservationId);
            boolean same=currentHash.equals(previousHash);
            if(!same) fullResponseHashCache.put(reservationId,currentHash);
            return same;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing string", e);
        }
    }
}
