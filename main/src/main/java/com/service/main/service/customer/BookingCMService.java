package com.service.main.service.customer;

import com.service.main.dto.*;
import com.service.main.entity.*;
import com.service.main.repository.*;
import com.service.main.service.PagingService;
import com.service.main.service.ScheduleService;
import com.service.main.service.StringGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StringGenerator stringGenerator;

    private final ModelMapper modelMapper = new ModelMapper();

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
        var optionalProperty = propertyRepository.findById(bookingDto.getPropertyId());
        if (optionalProperty.isEmpty()) {
            return new CustomResult(404, "Property not found", null);
        }
        Property property = optionalProperty.get();

        var optionalCustomer = userRepository.findById(bookingDto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return new CustomResult(404, "User not found", null);
        }
        User customer = optionalCustomer.get();

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
            if( property.getInstantBookRequirement() != null){
                var checkBadge = listUserBadge.stream()
                        .filter(userBadge -> userBadge.getBadge().getId().equals(property.getInstantBookRequirement().getId()) )
                        .findFirst()
                        .orElse(null);
                if (checkBadge == null) {

                    return new CustomResult(404, "Bagde not match", null);
                }
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
                if (dateDetails.getBooking().getStatus().equals("denied")) {
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

        Calendar calendarCheckIn = Calendar.getInstance();
        calendarCheckIn.setTime(bookingDto.getCheckInDay());
        calendarCheckIn.set(Calendar.HOUR_OF_DAY, Integer.parseInt(property.getCheckInAfter().split(":")[0])  );
        calendarCheckIn.set(Calendar.MINUTE, Integer.parseInt(property.getCheckInAfter().split(":")[1]) );
        booking.setCheckInDay(calendarCheckIn.getTime());

        Calendar calendarCheckOut = Calendar.getInstance();
        calendarCheckOut.setTime(bookingDto.getCheckOutDay());
        calendarCheckOut.set(Calendar.HOUR_OF_DAY, Integer.parseInt(property.getCheckOutBefore().split(":")[0])  );
        calendarCheckOut.set(Calendar.MINUTE, Integer.parseInt(property.getCheckOutBefore().split(":")[1]) );
        booking.setCheckOutDay(calendarCheckOut.getTime());


        String bookingCode = stringGenerator.generateRandomString();
        booking.setBookingCode(bookingCode);
        booking.setAmount(bookingDto.getAmount());
        // Check booking type for add booking status
        booking.setStatus("TRANSACTIONPENDDING");

        bookingRepository.save(booking);
        scheduleService.scheduleBookingTimeout(booking.getId(), booking.getCreatedAt());

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

                        String exceptionDateStr = sdf.format(exception.getDate());
                        String sqlDateStr = sdf.format(sqlDate);
                        return exceptionDateStr.equals(sqlDateStr);
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

        return new CustomResult(200, "Booking success", booking);
    }

    // Modelmapper to automatically map
    public CustomResult getBooking(int bookingId) {
        var optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            // Map entity sang DTO
            BookingResponseDto bookingDto = modelMapper.map(booking, BookingResponseDto.class);

            return new CustomResult(200, "Find success", bookingDto);
        }
        return new CustomResult(404, "Booking not found", null);
    }


    public CustomPaging getUserTrips(String email,
                                     int pageNumber,
                                     int pageSize,
                                     String status,
                                     String startDate,
                                     String endDate){
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null){
                var customPaging = new CustomPaging();
                customPaging.setStatus(404);
                customPaging.setMessage("Not found");
                return customPaging;
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            Date start = sf.parse(startDate);
            Date end = sf.parse(endDate);

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkInDay").descending());

            if(status.equals("checkout")){
                var bookings = bookingRepository.findUserCheckOutTrip(user.getId(), new Date(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }

            if(status.equals("stayin")){
                var bookings = bookingRepository.findUserCurrentlyStayInTrip(user.getId(), new Date(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }


            if(status.equals("upcoming")){
                var bookings = bookingRepository.findUserUpcomingTrip(user.getId(), new Date(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }

            if(status.equals("pending")){
                var bookings = bookingRepository.findUserPendingReviewTrip(user.getId(), new Date(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }

            if(status.equals("history")){
                var bookings = bookingRepository.findUserHistoryTrip(user.getId(), new Date(),  start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
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

    public CustomResult getTripCount(String email,
                                     String startDate,
                                     String endDate){
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null){
                return new CustomResult(404, "Not found", null);
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            Date start = sf.parse(startDate);
            Date end = sf.parse(endDate);

            var tripCount = bookingRepository.getTripCounts(user.getId(), start, end, new Date() );

            return new CustomResult(200, "OK", tripCount);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomPaging getUserReservationTrips(
                                     String email,
                                     int pageNumber,
                                     int pageSize,
                                     String status,
                                     String startDate,
                                     String endDate){
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null){
                var customPaging = new CustomPaging();
                customPaging.setStatus(404);
                customPaging.setMessage("Not found");
                return customPaging;
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            Date start = sf.parse(startDate);
            Date end = sf.parse(endDate);

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkInDay").descending());

            if(status.equals("pending")){
                var bookings = bookingRepository.findUserPendingReserved(user.getId(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }

            if(status.equals("denied")){
                var bookings = bookingRepository.findUserDeniedReservedTrip(user.getId(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
                return customPaging;
            }


            if(status.equals("cancel")){
                var bookings = bookingRepository.findUserCancelReservedTrip(user.getId(), start, end, pageable);

                var customPaging =  pagingService.convertToCustomPaging(bookings, pageNumber, pageSize);

                List<BookingDto> bookingsDtos = new ArrayList<>();

                for(var booking: (List<Booking>) customPaging.getData()){
                    var bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingsDtos.add(bookingDto);
                }
                customPaging.setData(bookingsDtos);
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

    public CustomResult getReservedCount(String email,
                                     String startDate,
                                     String endDate){
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null){
                return new CustomResult(404, "Not found", null);
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            Date start = sf.parse(startDate);
            Date end = sf.parse(endDate);

            var tripCount = bookingRepository.getReservedCount(user.getId(), start, end );

            return new CustomResult(200, "OK", tripCount);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

}
