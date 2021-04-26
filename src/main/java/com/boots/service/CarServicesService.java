package com.boots.service;

import com.boots.entity.CarServices;

import java.util.List;

public interface CarServicesService {
    boolean saveCarService(CarServices carService);
    List<CarServices> getAllCarServices();
    void deleteCarService(int id);
    CarServices findCarServiceById(int id);
}
