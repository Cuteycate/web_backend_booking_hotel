package com.example.hotel_booking_be_v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber; // Số điện thoại liên hệ
    private String email; // Email của khách sạn
    private String description;
    private int ratingCount;
    private float starRating; // Số sao trung bình (1-5)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id") // Liên kết với User có vai trò Rental
    private User owner;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_id")
    private Ward ward; // Liên kết với Ward

    private String street; // Địa chỉ chi tiết: số nhà, tên đường

    @Lob
    @JsonIgnore  // Không serialize khi trả về JSON
    private Blob coverPhoto; // Hình ảnh đại diện

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<HotelPhoto> photos = new ArrayList<>(); // Danh sách ảnh khác



}


