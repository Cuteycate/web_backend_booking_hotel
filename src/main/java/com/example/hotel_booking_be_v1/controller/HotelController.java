package com.example.hotel_booking_be_v1.controller;

import com.example.hotel_booking_be_v1.exception.PhotoRetrievalException;
import com.example.hotel_booking_be_v1.model.Hotel;
import com.example.hotel_booking_be_v1.model.HotelDTO;
import com.example.hotel_booking_be_v1.model.Room;
import com.example.hotel_booking_be_v1.repository.UserRepository;
import com.example.hotel_booking_be_v1.response.HotelResponse;
import com.example.hotel_booking_be_v1.service.DistrictService;
import com.example.hotel_booking_be_v1.service.HotelService;
import com.example.hotel_booking_be_v1.service.ProvinceService;
import com.example.hotel_booking_be_v1.service.WardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final UserRepository userRepository;

    // Đăng ký Hotel mới
    // Rental đăng ký Hotel mới
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<Hotel> registerHotel(
            @ModelAttribute HotelDTO hotelDTO,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException, SQLException {
        String ownerEmail = userDetails.getUsername();
        Hotel registeredHotel = hotelService.addHotel(hotelDTO, ownerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredHotel);
    }
    //update hotel
//    @PutMapping("/hotels/update/{id}")
//    public ResponseEntity<?> updateHotel(
//            @PathVariable Long id,
//            @RequestPart("hotel") @Valid HotelDTO hotelDto,
//            @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
//            @RequestPart(value = "newPhotos", required = false) MultipartFile[] newPhotos) {
//        try {
//            // Cập nhật thông tin ảnh đại diện (cover photo)
//            if (coverPhoto != null) {
//                hotelDto.setCoverPhoto(coverPhoto);  // Lưu ảnh đại diện mới nếu có
//            }
//            if (newPhotos != null) {
//                hotelDto.setPhotos(Arrays.asList(newPhotos));  // Lưu ảnh phụ mới nếu có
//            }
//
//            // Gửi đến service để xử lý cập nhật
//            Hotel updatedHotel = hotelService.updateHotel(id, hotelDto);
//
//            return ResponseEntity.ok(updatedHotel);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error updating hotel: " + e.getMessage());
//        }
//    }
    @PutMapping("/hotels/update/{id}")
    public ResponseEntity<?> updateHotel(
            @PathVariable Long id,
            @ModelAttribute HotelDTO hotelDTO) {
        try {
            // Tìm khách sạn theo ID
            Hotel updatedHotel = hotelService.updateHotel(id, hotelDTO);

            return ResponseEntity.ok(updatedHotel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating hotel: " + e.getMessage());
        }
    }



    @GetMapping("/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Hotel>> getPendingHotels() {
        List<Hotel> pendingHotels = hotelService.getHotelsByStatus("PENDING");
        return ResponseEntity.ok(pendingHotels);
    }

    // API phê duyệt khách sạn
    @PutMapping("/approve/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approveHotel(@PathVariable Long hotelId) {
        Hotel approvedHotel = hotelService.approveHotel(hotelId);
        return ResponseEntity.ok("Hotel approved successfully: " + approvedHotel.getName());
    }


    // Lấy danh sách các Hotel của chủ sở hữu
    @GetMapping("/my-hotels")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<List<HotelResponse>> getMyHotels(@AuthenticationPrincipal UserDetails userDetails) {
        String ownerEmail = userDetails.getUsername(); // Email lấy từ token
        List<Hotel> hotels = hotelService.getHotelsByOwner(ownerEmail); // Lấy khách sạn của người dùng

        List<HotelResponse> hotelResponses = new ArrayList<>();

        for (Hotel hotel : hotels) {
            byte[] photoBytes = null;
            Blob photoBlob = hotel.getCoverPhoto();  // Giả sử hotel có trường photo kiểu Blob
            if (photoBlob != null) {
                try {
                    photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
                } catch (SQLException e) {
                    throw new PhotoRetrievalException("Error retrieving photo for hotel with ID " + hotel.getId());
                }
            }

            // Chuyển đổi byte[] thành Base64 nếu có ảnh
            String base64Photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;

            HotelResponse hotelResponse = new HotelResponse(
                    hotel.getId(),
                    hotel.getName(),
                    hotel.getDescription(),
                    base64Photo,  // Trả về ảnh dưới dạng Base64
                    hotel.getStatus(),
                    hotel.getEmail(),
                    hotel.getPhoneNumber(),
                    hotel.getStreet(),
                    hotel.getWard() != null ? hotel.getWard().getName() : "N/A", // Phường
                    hotel.getWard() != null && hotel.getWard().getDistrict() != null ? hotel.getWard().getDistrict().getName() : "N/A", // Quận
                    hotel.getWard() != null && hotel.getWard().getDistrict() != null && hotel.getWard().getDistrict().getProvince() != null ? hotel.getWard().getDistrict().getProvince().getName() : "N/A" // Tỉnh
            );
            hotelResponses.add(hotelResponse);
        }

        // Trả về danh sách khách sạn kèm ảnh (nếu có)
        return ResponseEntity.ok(hotelResponses.isEmpty() ? Collections.emptyList() : hotelResponses);
    }
    // Admin từ chối Hotel

    @PutMapping("/reject/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Hotel> rejectHotel(@PathVariable Long hotelId) {
        Hotel rejectedHotel = hotelService.rejectHotel(hotelId);
        return ResponseEntity.ok(rejectedHotel);
    }



    // Lấy tất cả các Hotel
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // Xóa Hotel
    @DeleteMapping("/delete/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    // Thêm Room vào Hotel
    @PostMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ROLE_RENTAL')")
    public ResponseEntity<Hotel> addRoomToHotel(@PathVariable Long hotelId, @RequestBody Room room) {
        Hotel updatedHotel = hotelService.addRoomToHotel(hotelId, room);
        return ResponseEntity.ok(updatedHotel);
    }
    @GetMapping("/by-ward/{wardId}")
    public List<Hotel> getHotelsByWard(@PathVariable Long wardId) {
        return hotelService.findHotelsByWard(wardId);
    }

    @GetMapping("/by-district/{districtId}")
    public List<Hotel> getHotelsByDistrict(@PathVariable Long districtId) {
        return hotelService.findHotelsByDistrict(districtId);
    }

    @GetMapping("/by-province/{provinceId}")
    public List<Hotel> getHotelsByProvince(@PathVariable Long provinceId) {
        return hotelService.findHotelsByProvince(provinceId);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        try {
            Optional<Hotel> hotelOptional = hotelService.getHotelById(id);
            if (hotelOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found");
            }

            Hotel hotel = hotelOptional.get();
            List<String> encodedPhotos = new ArrayList<>();

            if (hotel.getPhotos() != null && !hotel.getPhotos().isEmpty()) {
                encodedPhotos = hotel.getPhotos().stream()
                        .filter(photo -> photo.getPhoto() != null) // Bỏ qua ảnh null
                        .map(photo -> encodePhoto(photo.getPhoto()))
                        .collect(Collectors.toList());
            }

            HotelResponse hotelResponse = new HotelResponse(
                    hotel.getId(),
                    hotel.getName(),
                    hotel.getDescription(),
                    encodePhoto(hotel.getCoverPhoto()),  // Ảnh đại diện
                    encodedPhotos,  // Danh sách ảnh phụ
                    hotel.getStatus(),
                    hotel.getEmail(),
                    hotel.getPhoneNumber(),
                    hotel.getStreet(),
                    hotel.getWard() != null ? hotel.getWard().getName() : "N/A",
                    hotel.getWard() != null && hotel.getWard().getDistrict() != null ? hotel.getWard().getDistrict().getName() : "N/A",
                    hotel.getWard() != null && hotel.getWard().getDistrict() != null && hotel.getWard().getDistrict().getProvince() != null ? hotel.getWard().getDistrict().getProvince().getName() : "N/A"
            );

            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving hotel: " + e.getMessage());
        }
    }

    // Phương thức mã hóa ảnh (không thay đổi)
    private String encodePhoto(Blob photoBlob) {
        if (photoBlob != null) {
            try {
                byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
                return Base64.encodeBase64String(photoBytes);
            } catch (SQLException e) {
                throw new RuntimeException("Error retrieving photo", e);
            }
        }
        return null;
    }

}
