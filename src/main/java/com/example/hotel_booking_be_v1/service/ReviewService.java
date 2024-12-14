package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.*;
import com.example.hotel_booking_be_v1.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService implements IReviewService{
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;


    public ReviewService(ReviewRepository reviewRepository, BookingRepository bookingRepository, ReviewPhotoRepository reviewPhotoRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.reviewPhotoRepository = reviewPhotoRepository;
    }

    public Review addReview(ReviewDTO reviewDTO) throws IOException, SQLException {
        boolean isEligible = bookingRepository.existsByBookingIdAndStatusAndCheckOutDateBefore(
                reviewDTO.getBookingId(), LocalDate.now()
        );

        if (!isEligible) {
            throw new IllegalArgumentException("User is not eligible to review this hotel.");
        }
        // Tạo đối tượng Review
        Review review = new Review();
        review.setBooking(bookingRepository.findById(reviewDTO.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + reviewDTO.getBookingId())));
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());

        // Lưu Review trước để có ID
        Review savedReview = reviewRepository.save(review);

        // Lưu các ảnh nếu có
        if (reviewDTO.getPhotos() != null) {
            for (MultipartFile photoFile : reviewDTO.getPhotos()) {
                ReviewPhoto photo = new ReviewPhoto();
                photo.setPhoto(new SerialBlob(photoFile.getBytes())); // Lưu ảnh dạng Blob
                photo.setReview(savedReview);
                reviewPhotoRepository.save(photo);
            }
        }

        return savedReview;
    }

    @Override
    public List<Review> findByHotelId(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
