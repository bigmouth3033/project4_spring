package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservedCountDto {
    private long pending;
    private long denied;
    private long cancel;
}
