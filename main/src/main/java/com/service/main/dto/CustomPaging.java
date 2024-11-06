package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CustomPaging {
    private int status;

    private String message;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    private long totalCount;

    private boolean hasPrevious;

    private boolean hasNext;

    public boolean isHasPrevious() {
        return currentPage > 0;
    }

    public boolean isHasNext() {
        return currentPage < totalPages;
    }

    private Object data;
}
