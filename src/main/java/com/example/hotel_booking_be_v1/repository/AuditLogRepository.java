package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByPerformedBy(String performedBy); // Tìm theo người thực hiện
}
