package com.service.chat.dto;

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
        return currentPage + 1 < totalPages;
    }

    private Object data;
}