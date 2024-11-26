package com.example.hotel_booking_be_v1.controller;

import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.Room;
import com.example.hotel_booking_be_v1.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;

    // Rental đăng ký Hotel mới
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<Hotel> registerHotel(@RequestBody Hotel hotel, @RequestParam Long ownerId) {
        Hotel registeredHotel = hotelService.addHotel(hotel, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredHotel);
    }

    // Admin phê duyệt Hotel
    @PutMapping("/approve/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Hotel> approveHotel(@PathVariable Long hotelId) {
        Hotel approvedHotel = hotelService.approveHotel(hotelId);
        return ResponseEntity.ok(approvedHotel);
    }


    // Lấy danh sách các khách sạn của Rental
    @GetMapping("/rental/{ownerId}")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<List<Hotel>> getHotelsByOwner(@PathVariable Long ownerId) {
        List<Hotel> hotels = hotelService.getHotelsByOwner(ownerId);
        return ResponseEntity.ok(hotels);
    }

    // Lấy tất cả các Hotel (Admin)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // Xóa Hotel
    @DeleteMapping("/delete/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.noContent().build();
    }
    // Admin từ chối Hotel
    @PutMapping("/reject/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Hotel> rejectHotel(@PathVariable Long hotelId) {
        Hotel rejectedHotel = hotelService.rejectHotel(hotelId);
        return ResponseEntity.ok(rejectedHotel);
    }
}

