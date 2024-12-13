package com.example.hotel_booking_be_v1.controller;

import com.example.hotel_booking_be_v1.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class HomeController {
    private final LocationService locationService;

    @Autowired
    public HomeController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/auto-complete")
    public ResponseEntity<?> getLocation(@RequestParam String query) {
        try {
            List<Map<String, String>> locations = locationService.searchLocations(query);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }
}
