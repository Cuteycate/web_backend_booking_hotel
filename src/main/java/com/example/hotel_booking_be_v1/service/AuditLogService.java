package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.AuditLog;
import com.example.hotel_booking_be_v1.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService{
    private final AuditLogRepository auditLogRepository;

    @Override
    public AuditLog addLog(String action, String performedBy) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setTimestamp(LocalDateTime.now());
        return auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getLogsByUser(String username) {
        return auditLogRepository.findByPerformedBy(username);
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
