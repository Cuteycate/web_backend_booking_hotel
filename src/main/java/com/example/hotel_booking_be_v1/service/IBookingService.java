package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
//    void cancelBooking(Long bookingId);

//    String saveBooking(Long roomId, BookedRoom bookingRequest);

//    BookedRoom findByBookingConfirmationCode(String confirmationCode);

//    List<BookedRoom> getAllBookings();
//    List<BookedRoom> getBookingsByUserEmail(String email);
    void saveBooking(Booking booking);

    List<Booking> findBookingsByUser(Long userId);

    List<Booking> findBookingsByHotel(Long hotelId);

    List<Booking> findBookingsByStatus(BookingStatus status);

    List<Booking> findBookingsBetweenDates(LocalDate startDate, LocalDate endDate);

    int countBookedRooms(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void saveBookingWithInvoice(Booking booking);
    List<Booking> findOverlappingBookings(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
