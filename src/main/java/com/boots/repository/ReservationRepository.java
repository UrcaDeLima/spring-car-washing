package com.boots.repository;

import com.boots.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findReservationById(int id);
    void deleteById(int id);
}
