package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByStatus(String status); // Tìm theo trạng thái (PENDING, APPROVED, REJECTED)
    List<Hotel> findByOwner_Id(Long ownerId); // Tìm khách sạn của một renter
    List<Hotel> findAllByOwnerId(Long ownerId);
}
