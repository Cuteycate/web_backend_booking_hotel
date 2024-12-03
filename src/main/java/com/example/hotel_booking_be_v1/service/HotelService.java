package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.exception.ResourceNotFoundException;
import com.example.hotel_booking_be_v1.model.*;
import com.example.hotel_booking_be_v1.repository.HotelRepository;
import com.example.hotel_booking_be_v1.repository.RoomRepository;
import com.example.hotel_booking_be_v1.repository.UserRepository;
import com.example.hotel_booking_be_v1.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final WardRepository wardRepository;

    @Override
    public Hotel addHotel(HotelDTO hotelDTO, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        // Tìm kiếm Ward từ database theo wardId
        Ward ward = wardRepository.findById(hotelDTO.getWardId())
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found"));

        // Chuyển DTO sang Entity
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setPhoneNumber(hotelDTO.getPhoneNumber());
        hotel.setEmail(hotelDTO.getEmail());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setStreet(hotelDTO.getStreet());
        hotel.setWard(ward); // Gán Ward vào Hotel
        hotel.setOwner(owner);
        hotel.setStatus("PENDING");

        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getHotelsByStatus(String status) {
        // Kiểm tra giá trị trạng thái
        if (!List.of("PENDING", "REJECTED").contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid hotel status: " + status);
        }
        return hotelRepository.findByStatus(status.toUpperCase());
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public List<Hotel> getHotelsByOwner(String ownerEmail) {
        // Tìm người dùng theo email
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Kiểm tra xem người dùng có vai trò 'ROLE_RENTAL' không
        boolean isRental = owner.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_RENTAL"));

        if (!isRental) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a rental");
        }

        // Lấy danh sách khách sạn của người sở hữu
        return hotelRepository.findAllByOwnerId(owner.getId());
    }
    @Override
    public Hotel approveHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        if (!"PENDING".equalsIgnoreCase(hotel.getStatus())) {
            throw new IllegalStateException("Only pending hotels can be approved");
        }
        hotel.setStatus("APPROVED");
        return hotelRepository.save(hotel);
    }

//    @Override
//    public Hotel approveHotel(Long hotelId) {
//        Hotel hotel = hotelRepository.findById(hotelId)
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
//
//        hotel.setStatus("APPROVED");
//        return hotelRepository.save(hotel);
//    }

    @Override
    public Hotel rejectHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        if ("REJECTED".equalsIgnoreCase(hotel.getStatus())) {
            throw new IllegalStateException("Hotel is already rejected");
        }

        hotel.setStatus("REJECTED");
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

        roomRepository.save(room);
        return hotelRepository.save(hotel);
    }
    // Tìm khách sạn theo xã/phường
    public List<Hotel> findHotelsByWard(Long wardId) {
        return hotelRepository.findByWardId(wardId);
    }

    // Tìm khách sạn theo quận/huyện
    public List<Hotel> findHotelsByDistrict(Long districtId) {
        return hotelRepository.findByDistrictId(districtId);
    }

    // Tìm khách sạn theo tỉnh/thành
    public List<Hotel> findHotelsByProvince(Long provinceId) {
        return hotelRepository.findByProvinceId(provinceId);
    }
}
