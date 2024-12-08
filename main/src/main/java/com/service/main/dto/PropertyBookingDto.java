package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBookingDto {
    private Integer id; // Có thể null nếu là booking mới
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Đảm bảo định dạng đúng
    private Date checkInDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Đảm bảo định dạng đúng
    private Date checkOutDay;
    private int totalPerson;
    private int children;
    private int adult;
    private double amount;
    private String bookingType;
    private String selfCheckInInstruction;
    private String status;
    private int propertyId; // ID của property được chọn
    private Integer refundPolicyId; // Có thể null
    private Integer hostReviewId; // Có thể null
    private Integer userReviewId; // Có thể null
    private Integer hostId; // ID của host
    private Integer customerId; // ID của customer
}
