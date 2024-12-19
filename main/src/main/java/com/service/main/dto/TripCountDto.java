package com.service.main.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripCountDto {
    private long checkoutCount;
    private long stayInCount;
    private long upcomingCount;
    private long pendingCount;
    private long historyCount;
}
