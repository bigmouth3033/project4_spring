package com.service.main.dto;

import com.service.main.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCountDto {
    private long currentlyHosting;
    private long checkoutHosting;
    private long checkInHosting;
    private long upcomingHosting;
    private long pendingReviewHosting;
    private long reservedHosting;
}
