package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.SearchResultDTO;
import com.example.hotel_booking_be_v1.repository.DistrictRepository;
import com.example.hotel_booking_be_v1.repository.HotelRepository;
import com.example.hotel_booking_be_v1.repository.ProvinceRepository;
import com.example.hotel_booking_be_v1.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    public List<SearchResultDTO> searchLocationsAndHotels(String query) {
        List<SearchResultDTO> results = new ArrayList<>();

        // Tính toán số lượng kết quả hiển thị tùy thuộc vào độ dài của chuỗi tìm kiếm
        int resultLimit = calculateResultLimit(query.length());

        // Tìm tỉnh và chuyển đổi thành SearchResultDTO
        provinceRepository.findByNameContaining(query).forEach(province -> {
            SearchResultDTO dto = new SearchResultDTO("Province", province.getName(), province.getId());
            results.add(dto);
        });

        // Tìm quận và chuyển đổi thành SearchResultDTO
        districtRepository.findByNameContaining(query).forEach(district -> {
            SearchResultDTO dto = new SearchResultDTO("District", district.getName(), district.getId());
            results.add(dto);
        });

        // Tìm phường và chuyển đổi thành SearchResultDTO
        wardRepository.findByNameContaining(query).forEach(ward -> {
            SearchResultDTO dto = new SearchResultDTO("Ward", ward.getName(), ward.getId());
            results.add(dto);
        });

        // Tìm khách sạn và chuyển đổi thành SearchResultDTO
        hotelRepository.findByNameContaining(query).forEach(hotel -> {
            SearchResultDTO dto = new SearchResultDTO("Hotel", hotel.getName(), hotel.getId());
            results.add(dto);
        });

        // Giới hạn kết quả trả về dựa trên độ dài của truy vấn
        if (results.size() > resultLimit) {
            return results.subList(0, resultLimit); // Giới hạn kết quả
        }

        return results;
    }

    // Phương thức để tính toán giới hạn kết quả theo độ dài của truy vấn
    private int calculateResultLimit(int queryLength) {
        if (queryLength <= 1) {
            return 12; // Nếu truy vấn rất ngắn, chỉ lấy tối đa 12 kết quả
        } else if (queryLength == 2) {
            return 16; // Nếu truy vấn có độ dài 2 ký tự, lấy tối đa 16 kết quả
        } else if (queryLength == 3) {
            return 20; // Truy vấn 3 ký tự, trả về tối đa 20 kết quả
        } else {
            return 25; // Truy vấn dài hơn, trả về tối đa 25 kết quả
        }
    }
}
