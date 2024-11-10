package com.service.main.service.customer;

import com.service.main.dto.BookingCountDto;
import com.service.main.dto.BookingDto;
import com.service.main.dto.CustomResult;
import com.service.main.entity.Booking;
import com.service.main.repository.BookingRepository;
import com.service.main.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingCMService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public CustomResult getBookingCount(String email){
        try{
            var user = userRepository.findUserByEmail(email);

            var bookingCount = bookingRepository.getBookingCounts(user.getId(), new Date());

            return new CustomResult(200, "Success", bookingCount);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }


    public CustomResult getBookings(String email, String status){
        try{
            var host = userRepository.findUserByEmail(email);

            if(status.equals("hosting")){
                var currentlyHosting = bookingRepository.getCurrentlyHostingBook(host.getId(), new Date());

                return getBookingCustomResult(currentlyHosting);
            }

            if(status.equals("checkout")){

                var checkoutBooks = bookingRepository.getCheckoutHostingBook(host.getId(), new Date());

                return getBookingCustomResult(checkoutBooks);
            }

            if(status.equals("soon")){
                var checkinBooks = bookingRepository.getCheckInHostingBook(host.getId(), new Date());
                return getBookingCustomResult(checkinBooks);
            }

            if(status.equals("upcoming")){
                var upcomingBook = bookingRepository.getUpcomingHostingBook(host.getId(), new Date());
                return getBookingCustomResult(upcomingBook);
            }

            if(status.equals("pending")){
                var upcomingBook = bookingRepository.getPendingReviewHostingBook(host.getId(), new Date());
                return getBookingCustomResult(upcomingBook);
            }

            return new CustomResult();
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getBookingOfProperty(int propertyId){
        try{
            var bookings = bookingRepository.findAllByPropertyId(propertyId);

             List<BookingDto> bookingsDto = new ArrayList<>();

             for(var booking : bookings){
                 var newBookingDto = new BookingDto();
                 BeanUtils.copyProperties(booking, newBookingDto);
                 bookingsDto.add(newBookingDto);
             }

            return new CustomResult(200, "Success", bookingsDto);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    private CustomResult getBookingCustomResult(List<Booking> checkoutBooks) {
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (var book : checkoutBooks){
            var bookingDto = new BookingDto();
            BeanUtils.copyProperties(book, bookingDto);
            bookingDtoList.add(bookingDto);
        }

        return new CustomResult(200, "Success", bookingDtoList);
    }
}
