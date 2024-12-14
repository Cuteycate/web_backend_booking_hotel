package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.Review;
import com.example.hotel_booking_be_v1.model.ReviewDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IReviewService {
    public Review addReview(ReviewDTO reviewDTO) throws IOException, SQLException;

    List<Review> findByHotelId(Long hotelId);

    List<Review> findByUserId(Long userId);
}
