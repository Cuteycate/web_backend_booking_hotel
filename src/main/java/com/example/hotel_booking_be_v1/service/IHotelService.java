package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.Room;

import java.util.List;
import java.util.Optional;

public interface IHotelService {
    Hotel addHotel(Hotel hotel, Long ownerId);

    Optional<Hotel> getHotelById(Long hotelId);

    List<Hotel> getHotelsByOwner(Long ownerId);

    Hotel approveHotel(Long hotelId);

    List<Hotel> getAllHotels();

    void deleteHotel(Long hotelId);

    Hotel addRoomToHotel(Long hotelId, Room room);
}
