package com.service.main.service.customer;

import com.service.main.dto.*;
import com.service.main.entity.Booking;
import com.service.main.entity.Review;
import com.service.main.repository.BookingRepository;
import com.service.main.repository.ReviewRepository;
import com.service.main.repository.UserRepository;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewCMService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PagingService pagingService;


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
            newRating.setBooking(booking.get());

            reviewRepository.save(newRating);


            booking.get().setHostReview(newRating);

            bookingRepository.save(booking.get());

            return new CustomResult(200, "Success", null);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }

    public CustomResult rateByCustomer(String email, RateByUserDto rateByHostDto){
        try{
            var user = userRepository.findUserByEmail(email);

            var booking = bookingRepository.findById(rateByHostDto.getBookingId());

            if(booking.isEmpty()){
                return new CustomResult(404, "Booking not found", null);
            }

            if(booking.get().getUserReview() != null){
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
            newRating.setBooking(booking.get());

            reviewRepository.save(newRating);


            booking.get().setUserReview(newRating);

            bookingRepository.save(booking.get());

            return new CustomResult(200, "Success", null);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }

    public CustomPaging getUserReview(
            String email,
            int pageNumber,
            int pageSize,
            String status
            ){
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null){
                var customPaging = new CustomPaging();
                customPaging.setStatus(404);
                customPaging.setMessage("Not found");
                return customPaging;
            }


            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

            if(status.equals("own")){
                var reviews = reviewRepository.getUserOwnReview(user.getId(), pageable);

                var customPaging =  pagingService.convertToCustomPaging(reviews, pageNumber, pageSize);

                List<ReviewDto> reviewDtos = new ArrayList<>();

                for(var review: (List<Review>) customPaging.getData()){
                    var reviewDto = new ReviewDto();
                    BeanUtils.copyProperties(review, reviewDto);
                    var userDto = new UserAuthDto();
                    BeanUtils.copyProperties(user, userDto);
                    reviewDto.setUser(userDto);
                    var hostDto =new UserAuthDto();
                    BeanUtils.copyProperties(userRepository.findUserById(review.getToUser()), hostDto);
                    reviewDto.setToUser(hostDto);

                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(review.getBooking(), bookingDto);
                    reviewDto.setBooking(bookingDto);

                    reviewDtos.add(reviewDto);
                }

                customPaging.setData(reviewDtos);

                return customPaging;
            }

            if(status.equals("other")){
                var reviews = reviewRepository.getUserReviewedByOther(user.getId(), pageable);

                var customPaging =  pagingService.convertToCustomPaging(reviews, pageNumber, pageSize);

                List<ReviewDto> reviewDtos = new ArrayList<>();

                for(var review: (List<Review>) customPaging.getData()){
                    var reviewDto = new ReviewDto();
                    BeanUtils.copyProperties(review, reviewDto);
                    var userDto = new UserAuthDto();
                    BeanUtils.copyProperties(user, userDto);
                    reviewDto.setToUser(userDto);
                    var hostDto =new UserAuthDto();
                    BeanUtils.copyProperties(userRepository.findUserById(review.getUser().getId()), hostDto);
                    reviewDto.setUser(hostDto);

                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(review.getBooking(), bookingDto);
                    reviewDto.setBooking(bookingDto);

                    reviewDtos.add(reviewDto);
                }

                customPaging.setData(reviewDtos);

                return customPaging;
            }

            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage("wrong status");
            return customPaging;

        }catch (Exception e){
            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }


}
