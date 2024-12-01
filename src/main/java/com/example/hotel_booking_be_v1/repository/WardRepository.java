package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    // Tìm xã/phường theo tên
    List<Ward> findByNameContaining(String name);

    // Tìm tất cả xã/phường thuộc một quận/huyện
    List<Ward> findByDistrictId(Long districtId);
}