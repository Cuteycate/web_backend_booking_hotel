package com.example.hotel_booking_be_v1.controller;

import com.example.hotel_booking_be_v1.exception.ResourceNotFoundException;
import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.HotelDTO;
import com.example.hotel_booking_be_v1.model.Room;
import com.example.hotel_booking_be_v1.model.User;
import com.example.hotel_booking_be_v1.repository.UserRepository;
import com.example.hotel_booking_be_v1.service.DistrictService;
import com.example.hotel_booking_be_v1.service.HotelService;
import com.example.hotel_booking_be_v1.service.ProvinceService;
import com.example.hotel_booking_be_v1.service.WardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final UserRepository userRepository;

    // Đăng ký Hotel mới
    // Rental đăng ký Hotel mới
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<Hotel> registerHotel(@RequestBody HotelDTO hotelDTO, @AuthenticationPrincipal UserDetails userDetails) {
        String ownerEmail = userDetails.getUsername(); // Email lấy từ token
        Hotel registeredHotel = hotelService.addHotel(hotelDTO, ownerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredHotel);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Hotel>> getPendingHotels() {
        List<Hotel> pendingHotels = hotelService.getHotelsByStatus("PENDING");
        return ResponseEntity.ok(pendingHotels);
    }

    // API phê duyệt khách sạn
    @PutMapping("/approve/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approveHotel(@PathVariable Long hotelId) {
        Hotel approvedHotel = hotelService.approveHotel(hotelId);
        return ResponseEntity.ok("Hotel approved successfully: " + approvedHotel.getName());
    }


    // Lấy danh sách các Hotel của chủ sở hữu
    @GetMapping("/my-hotels")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<List<Hotel>> getMyHotels(@AuthenticationPrincipal UserDetails userDetails) {
        String ownerEmail = userDetails.getUsername(); // Email lấy từ token
        List<Hotel> hotels = hotelService.getHotelsByOwner(ownerEmail);
        return ResponseEntity.ok(hotels.isEmpty() ? Collections.emptyList() : hotels);
    }



    // Admin từ chối Hotel
    @PutMapping("/reject/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Hotel> rejectHotel(@PathVariable Long hotelId) {
        Hotel rejectedHotel = hotelService.rejectHotel(hotelId);
        return ResponseEntity.ok(rejectedHotel);
    }

    // Lấy tất cả các Hotel
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

    // Thêm Room vào Hotel
    @PostMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<Hotel> addRoomToHotel(@PathVariable Long hotelId, @RequestBody Room room) {
        Hotel updatedHotel = hotelService.addRoomToHotel(hotelId, room);
        return ResponseEntity.ok(updatedHotel);
    }
    @GetMapping("/by-ward/{wardId}")
    public List<Hotel> getHotelsByWard(@PathVariable Long wardId) {
        return hotelService.findHotelsByWard(wardId);
    }

    @GetMapping("/by-district/{districtId}")
    public List<Hotel> getHotelsByDistrict(@PathVariable Long districtId) {
        return hotelService.findHotelsByDistrict(districtId);
    }

    @GetMapping("/by-province/{provinceId}")
    public List<Hotel> getHotelsByProvince(@PathVariable Long provinceId) {
        return hotelService.findHotelsByProvince(provinceId);
    }
}
