package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Booking;
import com.example.hotel_booking_be_v1.model.BookingStatus;
import com.example.hotel_booking_be_v1.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService  {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
//    public List<Booking> getAllBookingsByRoomId(Long roomId) {
//        return bookingRepository.findByRoomId(roomId);
//    }

//    @Override
//    public void cancelBooking(Long bookingId) {
//        bookingRepository.deleteById(bookingId);
//    }


//    @Override
//    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
//        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
//            throw  new InvalidBookingRequestException("Check-in date must come before check-out date");
//
//        }
//        Room room = roomService.getRoomById(roomId).get();
//        List<BookedRoom> existingBookings = room.getBookings();
//        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
//        if(roomIsAvailable){
//            room.addBooking(bookingRequest);
//            bookingRepository.save(bookingRequest);
//        }else {
//            throw new InvalidBookingRequestException("Sorry,This room is not available for the selected dates");
//        }
//        return bookingRequest.getBookingConfirmationCode();
//    }



//    @Override
//    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
//        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new ResourceNotFoundException("No booking found with booking code"+ confirmationCode));
//    }
//
//    @Override
//    public List<Booking> getAllBookings() {
//        return bookingRepository.findAll();
//    }
//
//    @Override
//    public List<BookedRoom> getBookingsByUserEmail(String email) {
//        return bookingRepository.findByGuestEmail(email);
//    }

//    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
//        return existingBookings.stream()
//                .noneMatch(existingBooking ->
//                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
//                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
//                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
//                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
//                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//
//                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
//                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//
//                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
//
//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
//
//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
//                );
//        return existingBookings.stream()
//                .noneMatch(existingBooking ->
//                        !(bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate()) ||
//                                bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate())));

//    }

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

}
