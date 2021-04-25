package com.boots.repository;

import com.boots.entity.CarServices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarServicesRepository extends JpaRepository<CarServices, Long> {
    CarServices findCarServiceById(int id);
}
