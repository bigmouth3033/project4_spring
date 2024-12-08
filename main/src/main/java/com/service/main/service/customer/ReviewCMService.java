package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.dto.RateByHostDto;
import com.service.main.entity.Review;
import com.service.main.repository.BookingRepository;
import com.service.main.repository.ReviewRepository;
import com.service.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewCMService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;


    public CustomResult rateByHost(String email, RateByHostDto rateByHostDto){
        try{
            var user = userRepository.findUserByEmail(email);

            var booking = bookingRepository.findById(rateByHostDto.getBookingId());

            if(booking.isEmpty()){
                return new CustomResult(404, "Booking not found", null);
            }

            if(booking.get().getHostReview() != null){
                return new CustomResult(403, "This booking is already reviewed", null);
            }

            var newRating = new Review();
            newRating.setUser(user);
            newRating.setTotalScore(rateByHostDto.getTotalScore());
            newRating.setReview(rateByHostDto.getReview());
            newRating.setAccuracyScore(rateByHostDto.getAccuracyScore());
            newRating.setCheckinScore(rateByHostDto.getCheckinScore());
            newRating.setCleanlinessScore(rateByHostDto.getCleanlinessScore());
            newRating.setCommunicationScore(rateByHostDto.getCommunicationScore());
            newRating.setToUser(rateByHostDto.getToUser());

            reviewRepository.save(newRating);


            booking.get().setHostReview(newRating);

            bookingRepository.save(booking.get());

            return new CustomResult(200, "Success", null);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }
}
