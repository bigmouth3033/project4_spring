package com.service.main.service.customer;

import com.service.main.dto.*;
import com.service.main.entity.*;
import com.service.main.repository.*;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BookingCMService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagingService pagingService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ManagedCityCMService managedCityCMService;

    @Autowired
    private BookDateDetailRepository bookDateDetailRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PropertyExceptionDateRepository propertyExceptionDateRepository;

    @Autowired
    private PropertyNotAvailableDateRepository propertyNotAvailableDateRepository;

    public CustomPaging getReservedBooking(String email, String status, int pageNumber, int pageSize, String startDate, String endDate, Integer propertyId) {
        try {
            var user = userRepository.findUserByEmail(email);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date start = null;
            Date end = null;

            if(startDate != null) {
                start = dateFormat.parse(startDate);
                end = dateFormat.parse(endDate);
            }

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkInDay"));
            var bookings = bookingRepository.findReservedBooking(user.getId(), start, end, propertyId, status, pageable);

            var customPaging = pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

            List<BookingDto> bookingsDtos = new ArrayList<>();

            for(var booking: (List<Booking>) customPaging.getData()){
                var bookingDto = new BookingDto();
                BeanUtils.copyProperties(booking, bookingDto);
                bookingsDtos.add(bookingDto);
            }
            customPaging.setData(bookingsDtos);
            return customPaging;


        }catch (Exception e){
            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }

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

    public CustomResult checkBookingConflict(int bookingId){
        try{
            var booking = bookingRepository.findById(bookingId);

            if(booking.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            var conflictList = bookingRepository.checkBookingConflict(bookingId, booking.get().getProperty().getId(), booking.get().getCheckInDay(), booking.get().getCheckOutDay()) ;

            List<BookingDto> bookingsDtos = new ArrayList<>();
            for(var bookingDto: conflictList){
                var bookingDtoDto = new BookingDto();
                BeanUtils.copyProperties(bookingDto, bookingDtoDto);
                bookingsDtos.add(bookingDtoDto);
            }

            return new CustomResult(200, "Success", bookingsDtos);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult denyBooking(int bookingId){
        try{
            var booking = bookingRepository.findById(bookingId);

            if(booking.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            if(!booking.get().getStatus().equals("PENDING") && !booking.get().getBookingType().equals("reserved")){
                return new CustomResult(403, "Booking error", null);
            }

            booking.get().setStatus("DENIED");

            bookingRepository.save(booking.get());

            return new CustomResult(200, "Success", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult acceptBooking(AcceptReservationBookingDto acceptReservationBookingDto){
        try{
            var booking = bookingRepository.findById(acceptReservationBookingDto.getBookingId());

            if(booking.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            if(!booking.get().getStatus().equals("PENDING") && !booking.get().getBookingType().equals("reserved")){
                return new CustomResult(403, "Booking error", null);
            }

            booking.get().setStatus("ACCEPT");

            bookingRepository.save(booking.get());

            for(var cancel : acceptReservationBookingDto.getCancelBookingIds()){
                var cancelBooking = bookingRepository.findById(cancel);

                if(cancelBooking.isEmpty()){
                    return new CustomResult(404, "Not found cancel booking", null);
                }

                if(!cancelBooking.get().getStatus().equals("PENDING") && !cancelBooking.get().getBookingType().equals("reserved")){
                    return new CustomResult(403, "Booking error", null);
                }

                cancelBooking.get().setStatus("DENIED");
                bookingRepository.save(cancelBooking.get());
            }

            return new CustomResult(200, "Success", null);

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

    // code giu

    public CustomResult createBooking(PropertyBookingDto bookingDto) {

        // Get Property
        var optionalProperty = propertyRepository.findById(bookingDto.getPropertyId());
        if (optionalProperty.isEmpty()) {
            return new CustomResult(404, "Property not found", null);
        }
        Property property = optionalProperty.get();

        // Get customer
        var optionalCustomer = userRepository.findById(bookingDto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return new CustomResult(404, "User not found", null);
        }
        User customer = optionalCustomer.get();

        // Get host
        var optionalHost = userRepository.findById(bookingDto.getHostId());
        if (optionalHost.isEmpty()) {
            return new CustomResult(404, "Host not found", null);
        }
        User host = optionalHost.get();
        // check host and customer
        if (Objects.equals(customer.getId(), host.getId())) {
            return new CustomResult(400, "Host cannot book their own property", null);
        }

        // Check booking type and badge
        if (property.getBookingType().equalsIgnoreCase("Instant")) {
            List<UserBadge> listUserBadge = (List<UserBadge>) customer.getUserBadges();

            var checkBadge = listUserBadge.stream()
                    .filter(userBadge -> userBadge.getBadge().getId() == property.getInstantBookRequirement().getId())
                    .findFirst()
                    .orElse(null);
            if (checkBadge == null) {

                return new CustomResult(404, "Bagde not match", null);
            }
        }
        // Get and check city available
        CustomResult cusCity = managedCityCMService.getManagedCity();

        List<ManagedCity> managedCities = (List<ManagedCity>) cusCity.getData();

        var checkCity = managedCities.stream()
                .filter(city -> city.getId() == property.getManagedCity().getId())
                .findFirst()
                .orElse(null);

        if (checkCity != null) {
        } else {
            return new CustomResult(404, "Manage city not found", null);

        }
        // Get exceptiondate
        Date startDate = bookingDto.getCheckInDay();
        Date endDate = bookingDto.getCheckOutDay();
        var optionalListNotAvailable = propertyNotAvailableDateRepository.findByPropertyId(property.getId());
        if (!optionalListNotAvailable.isEmpty()) {

            List<PropertyNotAvailableDate> listNotAvailabledate = (List<PropertyNotAvailableDate>) optionalListNotAvailable
                    .get();

            List<LocalDate> listDateBookingDto = new ArrayList<>();

            LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Generate all dates between check-in and check-out
            LocalDate currentDate = startLocalDate;
            while (!currentDate.isAfter(endLocalDate) && !currentDate.isEqual(endLocalDate)) {
                listDateBookingDto.add(currentDate);
                currentDate = currentDate.plusDays(1); // Add one day
            }
            boolean check = false; // Set to true initially, meaning the date is available

            for (PropertyNotAvailableDate unavailableDate : listNotAvailabledate) {
                LocalDate unavailableLocalDate = unavailableDate.getDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();

                for (LocalDate bookingDate : listDateBookingDto) {
                    if (unavailableLocalDate.equals(bookingDate)) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    return new CustomResult(400, "Not available date", null);
                }
            }
        }
        var optionalListBookDateDetail = bookDateDetailRepository.findByPropertyId(property.getId());
        if (optionalListBookDateDetail.isPresent()) {

            List<BookDateDetail> listBookDateDetail = optionalListBookDateDetail.get();

            List<LocalDate> listDateBookingDto = new ArrayList<>();

            LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Generate all dates between check-in and check-out
            LocalDate currentDate = startLocalDate;
            while (!currentDate.isAfter(endLocalDate) && !currentDate.isEqual(endLocalDate)) {
                listDateBookingDto.add(currentDate);
                currentDate = currentDate.plusDays(1); // Add one day
            }

            boolean check = false;

            for (BookDateDetail dateDetails : listBookDateDetail) {
                if (dateDetails.getBooking().getBookingType().equals("reserved")) {
                    continue;
                }
                LocalDate dateBooked = LocalDate.ofInstant(dateDetails.getNight().toInstant(), ZoneId.systemDefault());

                for (LocalDate bookingDate : listDateBookingDto) {
                    if (dateBooked.equals(bookingDate)) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    return new CustomResult(410, "Some days have been booked", dateBooked);
                }
            }
        }

        Booking booking = new Booking();

        BeanUtils.copyProperties(bookingDto, booking);

        booking.setRefundPolicy(property.getRefundPolicy());
        booking.setBookingType(property.getBookingType());
        booking.setCustomer(customer);
        booking.setHost(host);
        booking.setTotalPerson(bookingDto.getAdult() + bookingDto.getChildren());
        booking.setProperty(property);

        if (property.getBookingType().equalsIgnoreCase("instant")) {
            booking.setStatus("ACCEPT");
        } else if (property.getBookingType().equalsIgnoreCase("reserved")) {
            booking.setStatus("PENDING");
        }

        bookingRepository.save(booking);

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        List<PropertyExceptionDate> listException = propertyExceptionDateRepository.findAll();
        Date current = startDate;

        for (int i = 0; i < days; i++) {
            BookDateDetail dateDetail = new BookDateDetail();


            java.sql.Date sqlDate = new java.sql.Date(current.getTime());
            dateDetail.setNight(sqlDate);
            dateDetail.setBooking(booking);
            dateDetail.setProperty(property);

            String dateFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            PropertyExceptionDate exceptionDate = listException.stream()
                    .filter(exception -> {

                        String exceptionDateStr = sdf.format(exception.getDate()); // Giả sử exception.getDate() là java.sql.Date
                        String sqlDateStr = sdf.format(sqlDate); // Giả sử sqlDate là java.sql.Date
                        return exceptionDateStr.equals(sqlDateStr); // So sánh chuỗi ngày
                    })
                    .findFirst()
                    .orElse(null);

            if (exceptionDate != null) {
                dateDetail.setPrice(exceptionDate.getBasePrice());
            } else {
                dateDetail.setPrice(property.getBasePrice());
            }
            bookDateDetailRepository.save(dateDetail);

            current = new Date(current.getTime() + TimeUnit.DAYS.toMillis(1));
        }
        Transaction newTransaction = new Transaction();
        newTransaction.setBooking(booking);
        newTransaction.setAmount(bookingDto.getAmount());
        newTransaction.setUser(customer);
        newTransaction.setTransactionType("escrow");
        transactionRepository.save(newTransaction);
        return new CustomResult(200, "Booking success", booking);
    }

}
