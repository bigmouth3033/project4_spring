package com.service.main.service;

import com.service.main.dto.CustomResult;
import com.service.main.entity.BookDateDetail;
import com.service.main.entity.Booking;
import com.service.main.entity.Mail;
import com.service.main.entity.Transaction;
import com.service.main.repository.*;
import com.service.main.service.admin.MailADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private AuthenticationCodeRepository authenticationCodeRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookDateDetailRepository bookDateDetailRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailService mailService;

    private final Map<Integer, ScheduledFuture<?>> mailSendTasks = new ConcurrentHashMap<>();


    @Transactional
    @Scheduled(cron = "0 0 1 1 * ?")
    public void cleanupAuthenticationCode() {
        Date today = new Date();
        long yesterdayMillis = today.getTime() - (24 * 60 * 60 * 1000);
        Date yesterday = new Date(yesterdayMillis);
        authenticationCodeRepository.deleteExpiredCode(yesterday);
    }

    @PostConstruct
    public void reschedule() {
        List<Booking> pendingBookings = bookingRepository.findBookingByStatus("TRANSACTIONPENDDING");
        for (Booking booking : pendingBookings) {
            LocalDateTime executeTime = booking.getCreatedAt().plusMinutes(5);
            if (executeTime.isBefore(LocalDateTime.now())) {
                executeTaskImmediately(booking.getId());
            } else {
                scheduleBookingTimeout(booking.getId(), booking.getCreatedAt());
            }
        }


        List<Mail> pendingMail = mailRepository.getPendingMail(LocalDateTime.now());
        for(Mail mail : pendingMail) {
            LocalDateTime executeTime = mail.getSendDate();

            if (executeTime.isBefore(LocalDateTime.now())) {
                executeMailTaskImmediately(mail);
            } else {
                scheduleSendMail(mail);
            }
        }
    }

    public void executeMailTaskImmediately(Mail mail){
        List<String> toList = Arrays.stream(mail.getToList().split(",")).toList();

        for (String to : toList){
            mailService.sendMail(null, to, new String[]{to}, mail.getSubject(), mail.getBody());
        }

        mail.setSend(true);
        mailRepository.save(mail);
    }

    public void scheduleSendMail(Mail mail){
        Date executeAt = Date.from(mail.getSendDate().atZone(ZoneId.systemDefault()).toInstant());
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> executeMailTaskImmediately(mail), executeAt);
        mailSendTasks.put(mail.getId(), scheduledTask);
    }

    public void cancelMailSend(Integer mailId) {
        ScheduledFuture<?> scheduledTask = mailSendTasks.get(mailId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            mailSendTasks.remove(mailId);
        }
    }

    public void reScheduleMail(Mail mail) {
        ScheduledFuture<?> scheduledTask = mailSendTasks.get(mail.getId());
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            mailSendTasks.remove((mail.getId()));
            scheduleSendMail(mail);
        }
    }



    public void scheduleBookingTimeout(Integer bookingId, LocalDateTime bookingTime) {
        Date executeAt = Date.from(bookingTime.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> executeTaskBooking(bookingId), executeAt);
    }

    private void executeTaskImmediately(Integer bookingId) {
        executeTaskBooking(bookingId);
    }

    private void executeTaskBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if(booking != null) {
            var listTransaction = transactionRepository.findByBookingId(bookingId);
            var listBookDateDetail = bookDateDetailRepository.findByPropertyIdAndBookingId(bookingId);


            if (listTransaction.isEmpty()) {
                bookDateDetailRepository.deleteAll(listBookDateDetail.get());
                bookingRepository.delete(booking);
                return;
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupReservation() {
        var getExpiredReservationBooking = bookingRepository.getExpiredReservationBooking(new Date());

        if(getExpiredReservationBooking != null) {
            for(var booking : getExpiredReservationBooking) {
                booking.setStatus("DENIED");
                bookingRepository.save(booking);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void finishPayment() {
        var getReadyToFinishPayment = bookingRepository.getReadyToFinishPayment(new Date());

        if(getReadyToFinishPayment != null) {
            for(var booking : getReadyToFinishPayment) {
                var newHostPayment = new Transaction();
                newHostPayment.setAmount(booking.getAmount() * 0.9);
                newHostPayment.setBooking(booking);
                newHostPayment.setUser(null);
                newHostPayment.setTransferOn(new Date());
                newHostPayment.setTransactionType("host_fee");


                var websitePayment = new Transaction();
                websitePayment.setAmount(booking.getAmount() * 0.1);
                websitePayment.setBooking(booking);
                websitePayment.setUser(null);
                websitePayment.setTransferOn(new Date());
                websitePayment.setTransactionType("website_fee");

                transactionRepository.save(newHostPayment);
                transactionRepository.save(websitePayment);
            }
        }
    }


}
