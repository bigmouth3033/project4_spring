package com.service.main.service.azure.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MailPayload {
    private MultipartFile[] file;
    private String to;
    private String[] cc;
    private String subject;
    private String body;
}
