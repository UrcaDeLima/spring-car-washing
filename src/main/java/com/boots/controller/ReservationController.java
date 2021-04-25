package com.boots.controller;

import com.boots.entity.CarServices;
import com.boots.entity.Reservation;
import com.boots.entity.User;
import com.boots.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {
	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("/reservation")
	public String getAllReservations(Model model) {
		List<Reservation> reservations;
		reservations = reservationService.getAllReservations();
		model.addAttribute("reservations", reservations);

		return "reservation";
	}

	@GetMapping("/reservation/{id}")
	public String getReservationById(@PathVariable(value = "id") int reservationId, Model model) {
		Map<String, Object> map = reservationService.getReservationQueue(reservationId);
		if(map.get("reservation") == null){
			model.addAttribute("Error", "Ошибка, неверный id");
			return "error";
		}
		model.addAttribute("placeQueue", map.get("placeQueue"));
		model.addAttribute("timeToService", map.get("timeToService"));
		model.addAttribute("reservation", map.get("reservation"));

		return "personalReservation";
	}

	@PostMapping("/createReservation")
	public String createReservation(
			@RequestParam(required = true, defaultValue = "" ) int carServiceId,
			@RequestParam(required = true, defaultValue = "" ) String date,
			Model model
	) throws ParseException {
		Reservation newReservation = new Reservation();
		CarServices carServices = new CarServices();
		User user = new User();
		user.setId(2L);
		carServices.setId(carServiceId);
		newReservation.setUsers(user);
		newReservation.setCarServices(carServices);

		SimpleDateFormat MY_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date from = MY_SIMPLE_DATE_FORMAT.parse(date);
		newReservation.setReservation_time(from);

		if (!reservationService.saveReservation(newReservation)){
			model.addAttribute("Error", "Ошибка, проверьте правильность введённых данных");
			return "newReservation";
		}

		return "redirect:reservation";
	}

	@PostMapping("/deleteReservation")
	public String deleteReservation(
			@RequestParam(required = true, defaultValue = "" ) int reservationId,
			@RequestParam(required = true, defaultValue = "" ) String action
	) {
		if (action.equals("delete")){
			reservationService.deleteReservation(reservationId);
		}
		return "redirect:/reservation";
	}
}

