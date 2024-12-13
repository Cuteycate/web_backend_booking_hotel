package com.example.hotel_booking_be_v1.controller;


import com.example.hotel_booking_be_v1.exception.InvalidBookingRequestException;
import com.example.hotel_booking_be_v1.exception.ResourceNotFoundException;
import com.example.hotel_booking_be_v1.model.*;
import com.example.hotel_booking_be_v1.response.BookingResponse;
import com.example.hotel_booking_be_v1.response.RoomResponse;
import com.example.hotel_booking_be_v1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final IUserService userService;
    private final IHotelService hotelService;


//    @GetMapping("/all-bookings")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<List<BookingResponse>> getAllBookings(){
//        List<BookedRoom> bookings = bookingService.getAllBookings();
//        List<BookingResponse> bookingResponses = new ArrayList<>();
//        for (BookedRoom booking: bookings){
//            BookingResponse bookingResponse = getBookingResponse(booking);
//            bookingResponses.add(bookingResponse);
//        }
//        return ResponseEntity.ok(bookingResponses);
//    }


//    @GetMapping ("/confirmation/{confirmationCode}")
//    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
//        try{
//            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
//            BookingResponse bookingResponse = getBookingResponse(booking);
//            return ResponseEntity.ok(bookingResponse);
//        }catch (ResourceNotFoundException ex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//
//        }
//    }
//    @PostMapping("/room/{roomId}/booking")
//    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
//                                         @RequestBody BookedRoom bookingRequest){
//        try {
//            String confirmationCode = bookingService.saveBooking(roomId,bookingRequest);
//            return ResponseEntity.ok("Room booked successfully! Your booking confirmation code is:"+confirmationCode);
//
//
//        }catch (InvalidBookingRequestException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//
//        }
//    }

//    private BookingResponse getBookingResponse(BookedRoom booking) {
//        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
//        RoomResponse room = new RoomResponse(
//                theRoom.getId(),
//                theRoom.getRoomType(),
//                theRoom.getRoomPrice());
//        return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(),booking.getCheckOutDate(),booking.getGuestFullName(),booking.getGuestEmail(),booking.getNumOfAdults(),booking.getNumOfChildren(),booking.getTotalNumOfGuest(),booking.getBookingConfirmationCode(),room);
//    }
//    @GetMapping("/user/{email}/bookings")
//    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
//        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
//        List<BookingResponse> bookingResponses = new ArrayList<>();
//        for (BookedRoom booking : bookings) {
//            BookingResponse bookingResponse = getBookingResponse(booking);
//            bookingResponses.add(bookingResponse);
//        }
//        return ResponseEntity.ok(bookingResponses);
//    }

//    @DeleteMapping("/booking/{bookingId}/delete")
//    public void cancelBooking(@PathVariable Long bookingId){
//        bookingService.cancelBooking(bookingId);
//    }

    @PostMapping("/add")
    public ResponseEntity<?> addBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            // Tạo đối tượng Booking từ BookingDTO
            Booking booking = new Booking();
            booking.setCheckInDate(bookingDTO.getCheckInDate());
            booking.setCheckOutDate(bookingDTO.getCheckOutDate());
            booking.setNumberOfGuests(bookingDTO.getNumberOfGuests());
            booking.setStatus(bookingDTO.getStatus());

            // Lấy đối tượng User từ userId trong DTO
            User user = userService.getUserById(bookingDTO.getUserId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            booking.setUser(user);

            // Lấy đối tượng Hotel từ hotelId trong DTO
            Hotel hotel = hotelService.getHotelById1(bookingDTO.getHotelId());
            if (hotel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found.");
            }
            booking.setHotel(hotel);

            // Lấy danh sách Room từ roomIds trong DTO
            List<Room> rooms = roomService.getAllRoomByIds(bookingDTO.getRoomIds());
            if (rooms.size() != bookingDTO.getRoomIds().size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Some rooms not found.");
            }
            booking.setRooms(rooms);

            // Tính tổng giá dựa trên các phòng (giả sử mỗi phòng có giá)
            BigDecimal totalPrice = rooms.stream()
                    .map(Room::getRoomPrice) // Lấy giá phòng (BigDecimal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            booking.setTotalPrice(totalPrice);

            // Lưu Booking vào cơ sở dữ liệu
            bookingService.saveBooking(booking);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Booking created successfully.");
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
