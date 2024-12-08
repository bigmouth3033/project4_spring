package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptReservationBookingDto {

    private Integer bookingId;

    private List<Integer> cancelBookingIds;
}
