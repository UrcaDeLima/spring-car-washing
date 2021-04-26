package com.boots.controller;

import com.boots.entity.CarServices;
import com.boots.service.CarServicesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class CarServicesController {
	private final CarServicesServiceImpl carServicesServiceImpl;

	@Autowired
	public CarServicesController(CarServicesServiceImpl carServicesServiceImpl) {
		this.carServicesServiceImpl = carServicesServiceImpl;
	}

	@GetMapping("/carService")
	public String getAllCarServices(Model model) {
		List<CarServices> carServices;
		carServices = carServicesServiceImpl.getAllCarServices();
		model.addAttribute("carServices", carServices);

		return "carServices";
	}

	@PostMapping("/createCarService")
	public String createCarService(
			@RequestParam(required = true, defaultValue = "" ) int execution_time,
			@RequestParam(required = true, defaultValue = "" ) int price,
			@RequestParam(required = true, defaultValue = "" ) String title,
			Model model
	) {
		CarServices newCarService = new CarServices(title, execution_time, price);

		if (!carServicesServiceImpl.saveCarService(newCarService)){
			model.addAttribute("Error", "Ошибка, проверьте правильность введённых данных");
			return "newcCarService";
		}

		return "redirect:carService";
	}

	@PostMapping("/deleteCarService")
	public String deleteCarService(
			@RequestParam(required = true, defaultValue = "" ) int carServiceId,
			@RequestParam(required = true, defaultValue = "" ) String action)
	{
		if (action.equals("delete")){
			carServicesServiceImpl.deleteCarService(carServiceId);
		}
		return "redirect:/carService";
	}
}

