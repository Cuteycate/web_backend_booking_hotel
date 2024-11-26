package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.AuditLog;

import java.util.List;

public interface IAuditLogService {
    AuditLog addLog(String action, String performedBy);
    List<AuditLog> getLogsByUser(String username);
    List<AuditLog> getAllLogs();
}
