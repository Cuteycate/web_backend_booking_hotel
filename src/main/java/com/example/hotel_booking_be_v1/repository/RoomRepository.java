package com.example.hotel_booking_be_v1.repository;

import com.example.hotel_booking_be_v1.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {
//    @Query("SELECT DISTINCT r.roomType FROM Room r")
//    List<String> findDistinctRoomTypes();
//    @Query(" SELECT r FROM Room r " +
//            " WHERE r.roomType LIKE %:roomType% " +
//            " AND r.id NOT IN (" +
//            "  SELECT br.room.id FROM BookedRoom br " +
//            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
//            ")")
//    List<Room> findAvailableRoomByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
//    List<Room> findByHotel_Id(Long hotelId); // Lấy danh sách room theo hotel
//    List<Room> findAllByHotelId(Long hotelId);

    List<Room> findByHotelId(Long hotelId);
}
