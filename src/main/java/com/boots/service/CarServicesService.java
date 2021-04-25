package com.boots.service;

import com.boots.entity.CarServices;
import com.boots.repository.CarServicesRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;


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

    @Transactional
    public void deleteCarService(int id) {
        if (findCarServiceById(id) != null) {
            carServicesRepository.deleteById(id);
        }
    }

    public boolean saveCarService(CarServices carService) {
        carServicesRepository.save(carService);
        return true;
    }

    public List<CarServices> getAllCarServices() {
        return carServicesRepository.findAll();
    }
}
