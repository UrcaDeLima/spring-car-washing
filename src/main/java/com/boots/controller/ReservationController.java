package com.boots.controller;

import com.boots.entity.Reservation;
import com.boots.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

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
		List<Object> list = reservationService.getReservationQueue(reservationId);
		if(list.get(2) == null){
			model.addAttribute("Error", "Ошибка, неверный id");
			return "error";
		}
		model.addAttribute("placeQueue", list.get(0));
		model.addAttribute("timeToService", list.get(1));
		model.addAttribute("reservation", list.get(2));

		return "personalReservation";
	}

	@PostMapping("/createReservation")
	public String createReservation(
			@RequestBody Reservation reservation,
			Model model
	) throws ParseException {
		if (!reservationService.saveReservation(reservation)){
			model.addAttribute("Error", "Ошибка, проверьте правильность введённых данных");
			return "newReservation";
		}

		return "redirect:reservation";
	}

	@PostMapping("/deleteReservation")
	public String deleteReservation(@RequestParam(required = true, defaultValue = "" ) int reservationId,
							 @RequestParam(required = true, defaultValue = "" ) String action) {
		System.out.println(reservationId);
		if (action.equals("delete")){
			reservationService.deleteReservation(reservationId);
			return "redirect:/reservation";
		}
		return "redirect:/reservation";
	}
}

