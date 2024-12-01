package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.Room;

import java.util.List;
import java.util.Optional;

    public interface IHotelService {
        Hotel addHotel(Hotel hotel, String ownerEmail);

    Optional<Hotel> getHotelById(Long hotelId);

    List<Hotel> getHotelsByOwner();

    Hotel approveHotel(Long hotelId);

    Hotel rejectHotel(Long hotelId);

    List<Hotel> getAllHotels();

    void deleteHotel(Long hotelId);

    Hotel addRoomToHotel(Long hotelId, Room room);

    List<Hotel> getHotelsByStatus(String status);

    public List<Hotel> findHotelsByWard(Long wardId);

    // Tìm khách sạn theo quận/huyện
    public List<Hotel> findHotelsByDistrict(Long districtId);

    // Tìm khách sạn theo tỉnh/thành
    public List<Hotel> findHotelsByProvince(Long provinceId);

}
