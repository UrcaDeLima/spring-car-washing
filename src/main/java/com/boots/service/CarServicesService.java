package com.boots.service;

import com.boots.entity.CarServices;
import com.boots.repository.CarServicesRepository;
import org.springframework.stereotype.Service;


@Service
public class CarServicesService {
    final
    CarServicesRepository carServicesRepository;

    public CarServicesService(CarServicesRepository carServicesRepository) {
        this.carServicesRepository = carServicesRepository;
    }

    public CarServices findCarServiceById(int id) {
        return carServicesRepository.findCarServiceById(id);
    }
}
