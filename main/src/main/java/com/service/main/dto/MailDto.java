package com.service.main.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {
    private Integer id;

    private String body;

    private String subject;

    private String toList;

    private List<MultipartFile> files;

    private String sendDate;

    private boolean isSend;

}
