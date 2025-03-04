package fr.hb.restaurant.controller;

import fr.hb.restaurant.model.Reservation;
import fr.hb.restaurant.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public String getAllReservations(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservations/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Reservation reservation = new Reservation();
        model.addAttribute("reservation", reservation);
        model.addAttribute("action", "add");
        return "reservations/form";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute @Valid Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "add");
            return "reservations/form";
        }

        // Vérification de la contrainte
        String errorMessage = reservationService.save(reservation);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("action", "add");
            return "reservations/form";
        }
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Reservation reservation = reservationService.findById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            model.addAttribute("action", "edit");
            return "reservations/form";
        }
        return "redirect:/reservations";
    }

    @PostMapping("/edit")
    public String editReservation(@ModelAttribute @Valid Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            return "reservations/form";
        }

        String errorMessage = reservationService.save(reservation);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("action", "edit");
            return "reservations/form";
        }

        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable int id) {
        reservationService.delete(id);
        return "redirect:/reservations";
    }
}
