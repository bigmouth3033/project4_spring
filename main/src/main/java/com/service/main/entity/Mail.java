package com.service.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String toList;

    @Column(columnDefinition = "TEXT")
    private String files;

    private LocalDateTime sendDate;

    private boolean isSend;

    private Date createdAt = new Date();
}
