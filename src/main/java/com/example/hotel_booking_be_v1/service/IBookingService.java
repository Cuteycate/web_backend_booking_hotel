package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;

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
}
