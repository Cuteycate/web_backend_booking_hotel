package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.exception.ResourceNotFoundException;
import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.Room;
import com.example.hotel_booking_be_v1.model.User;
import com.example.hotel_booking_be_v1.repository.HotelRepository;
import com.example.hotel_booking_be_v1.repository.RoomRepository;
import com.example.hotel_booking_be_v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService{
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Hotel addHotel(Hotel hotel, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        hotel.setOwner(owner);
        hotel.setStatus("PENDING"); // Ban đầu đặt trạng thái là chờ duyệt
        return hotelRepository.save(hotel);
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public List<Hotel> getHotelsByOwner(Long ownerId) {
        return hotelRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public Hotel approveHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotel.setStatus("APPROVED");
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotelRepository.delete(hotel);
    }

    @Override
    public Hotel addRoomToHotel(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        if (!hotel.getStatus().equalsIgnoreCase("APPROVED")) {
            throw new IllegalStateException("Cannot add room to a hotel that is not approved");
        }

        room.setHotel(hotel);
        hotel.getRooms().add(room);

        roomRepository.save(room); // Lưu room vào database
        return hotelRepository.save(hotel); // Cập nhật hotel với room mới
    }
}
