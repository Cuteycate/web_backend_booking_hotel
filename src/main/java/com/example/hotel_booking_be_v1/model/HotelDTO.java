package com.example.hotel_booking_be_v1.model;

import lombok.Data;

@Data
public class HotelDTO {
    private String name;         // Tên khách sạn
    private String phoneNumber;  // Số điện thoại
    private String email;        // Email khách sạn
    private String description;  // Mô tả khách san
    private String street;       // Địa chỉ chi tiết
    private Long wardId;        // Mã code của Ward
}
