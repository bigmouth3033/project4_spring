package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.dto.TransactionDto;
import com.service.main.entity.Transaction;
import com.service.main.repository.BookingRepository;
import com.service.main.repository.TransactionRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionCmService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public CustomResult createTransaction(TransactionDto transactionDto) {

        var bookingOptional = bookingRepository.findById(transactionDto.getBookingId());
        if (bookingOptional.isEmpty()) {
            return new CustomResult(404, "Booking not found", null);
        }

        var booking = bookingOptional.get();
        var transaction = new Transaction();
        if ("TRANSACTIONPENDDING".equals(booking.getStatus())) {

            transaction.setBooking(booking);
            transaction.setAmount(transactionDto.getAmount());
            transaction.setUser(booking.getCustomer());
            transaction.setTransactionType("escrow");
            transaction.setTransferOn(new Date());
            transactionRepository.save(transaction);

            if(booking.getBookingType().equals("reserved")){
                booking.setStatus("PENDING");
            }else{
                booking.setStatus("ACCEPT");
            }

            bookingRepository.save(booking);

            return new CustomResult(200, "Transaction success", transaction);
        }
        return new CustomResult(400,
                "The transaction failed because it had already been successfully paid for.", null);

    }
}
