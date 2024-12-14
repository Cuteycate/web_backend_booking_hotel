package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByUserId(Long userId); // Tìm đặt phòng theo người dùng
    List<Booking> findByHotelId(Long hotelId); // Tìm đặt phòng theo khách sạn
    List<Booking> findByStatus(BookingStatus status); // Tìm đặt phòng theo trạng thái
    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT COUNT(r.id) " +
            "FROM Booking b " +
            "JOIN b.rooms r " +
            "WHERE r.id = :roomId " +
            "AND ((:checkInDate BETWEEN b.checkInDate AND b.checkOutDate) " +
            "OR (:checkOutDate BETWEEN b.checkInDate AND b.checkOutDate) " +
            "OR (b.checkInDate BETWEEN :checkInDate AND :checkOutDate))")
    int countBookedRooms(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.id = :bookingId " +
            "AND b.status = 'COMPLETED' " +
            "AND b.checkOutDate < :currentDate")
    boolean existsByBookingIdAndStatusAndCheckOutDateBefore(
            @Param("bookingId") Long bookingId,
            @Param("currentDate") LocalDate currentDate
    );
}
