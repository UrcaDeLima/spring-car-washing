package com.boots.service;

import com.boots.entity.Reservation;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    Reservation findReservationById(int reservationId);
    Map<String, Object> getReservationQueue(int id);
    List<Reservation> findAllReservationByUserId(Long userId);
    Reservation findReservationByIdAndUserId(int reservationId, Long userId);
    List<Reservation> getAllReservations();
    boolean saveReservation(Reservation reservation) throws ParseException;
    void deleteReservation(int id);
}
