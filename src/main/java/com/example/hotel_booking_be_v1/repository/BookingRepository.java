package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
//    List<Booking> findByRoomId(Long roomId);
//
//   Optional <Booking> findByBookingConfirmationCode(String confirmationCode);
//
//    List<Booking> findByGuestEmail(String email);
    List<Booking> findByUserId(Long userId); // Tìm đặt phòng theo người dùng
    List<Booking> findByHotelId(Long hotelId); // Tìm đặt phòng theo khách sạn
    List<Booking> findByStatus(BookingStatus status); // Tìm đặt phòng theo trạng thái
    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
}
