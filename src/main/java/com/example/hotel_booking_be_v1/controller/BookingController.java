package com.example.hotel_booking_be_v1.controller;


import com.example.hotel_booking_be_v1.model.*;
import com.example.hotel_booking_be_v1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final IUserService userService;
    private final IHotelService hotelService;


    @PostMapping("/add")
    public ResponseEntity<?> addBooking(@AuthenticationPrincipal UserDetails userDetails,
                                        @ModelAttribute BookingDTO bookingDTO) {
        try {
            // Lấy email từ userDetails
            String email = userDetails.getUsername();

            // Tìm user bằng email
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            // Tạo đối tượng Booking từ BookingDTO
            Booking booking = new Booking();
            booking.setCheckInDate(bookingDTO.getCheckInDate());
            booking.setCheckOutDate(bookingDTO.getCheckOutDate());
            booking.setNumberOfGuests(bookingDTO.getNumberOfGuests());
            booking.setStatus(bookingDTO.getStatus());
            booking.setUser(user); // Gán User lấy từ token

            // Lấy đối tượng Hotel từ hotelId trong DTO
            Hotel hotel = hotelService.getHotelById1(bookingDTO.getHotelId());
            if (hotel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found.");
            }
            booking.setHotel(hotel);

            List<Long> roomIds = bookingDTO.getRoomIds();

            // Đếm số lần xuất hiện của từng roomId trong roomIds
            Map<Long, Long> roomCountMap = roomIds.stream()
                    .collect(Collectors.groupingBy(roomId -> roomId, Collectors.counting()));

            // Lấy danh sách phòng từ cơ sở dữ liệu
            List<Room> rooms = roomService.getAllRoomByIds(new ArrayList<>(roomCountMap.keySet()));

            // Kiểm tra số lượng phòng có sẵn cho từng loại phòng (theo roomIds)
            List<Room> bookingRooms = new ArrayList<>();
            for (Map.Entry<Long, Long> entry : roomCountMap.entrySet()) {
                Long roomId = entry.getKey();
                Long roomCountRequested = entry.getValue();

                // Tìm phòng theo roomId
                Room room = rooms.stream()
                        .filter(r -> r.getId().equals(roomId))
                        .findFirst()
                        .orElse(null);

                if (room == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + roomId + " not found.");
                }

                // Kiểm tra số lượng phòng đã được đặt trong khoảng thời gian check-in và check-out
                List<Booking> overlappingBookings = bookingService.findOverlappingBookings(roomId, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
                long roomBookedCount = overlappingBookings.stream()
                        .flatMap(b -> b.getRooms().stream())
                        .filter(r -> r.getId().equals(roomId))
                        .count();

                // Kiểm tra số lượng phòng còn trống
                if (room.getQuantity() - roomBookedCount < roomCountRequested) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Not enough rooms available for room ID " + roomId + ". Only " + (room.getQuantity() - roomBookedCount) + " rooms available.");
                }

                // Thêm phòng vào danh sách bookingRooms
                for (int i = 0; i < roomCountRequested; i++) {
                    bookingRooms.add(room);
                }
            }
            booking.setRooms(bookingRooms);

            // Tính số ngày thuê và đảm bảo check-out phải sau check-in
            long days = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
            if (days <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Check-out date must be after check-in date.");
            }

            // Lưu Booking cùng với hóa đơn (Invoice)
            bookingService.saveBookingWithInvoice(booking);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Booking created successfully with invoice.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.findBookingsByUser(userId));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Booking>> getBookingsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.findBookingsByHotel(hotelId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.findBookingsByStatus(status));
    }
}
