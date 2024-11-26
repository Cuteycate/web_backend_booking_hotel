package com.example.hotel_booking_be_v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Hotel sẽ được liên kết với user có role phù hợp ( rental )
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String description;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id") // Liên kết với User có vai trò Rental
    private User owner;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
}

