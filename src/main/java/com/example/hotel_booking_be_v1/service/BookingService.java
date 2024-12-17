package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;
import com.example.hotel_booking_be_v1.model.Invoice;
import com.example.hotel_booking_be_v1.repository.BookingRepository;
import com.example.hotel_booking_be_v1.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService  {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final InvoiceService invoiceService; // Tiêm InvoiceService vào

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public List<Booking> findBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> findBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    public List<Booking> findBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> findBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByCheckInDateBetween(startDate, endDate);
    }

    @Override
    public int countBookedRooms(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookingRepository.countBookedRooms(roomId, checkInDate, checkOutDate);
    }

    @Override
    public void saveBookingWithInvoice(Booking booking) {
        Invoice invoice = invoiceService.createInvoiceForBooking(booking);
        booking.setInvoice(invoice);
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findOverlappingBookings(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookingRepository.findBookingsByRoomAndDateRange(roomId, checkInDate, checkOutDate);
    }
}
