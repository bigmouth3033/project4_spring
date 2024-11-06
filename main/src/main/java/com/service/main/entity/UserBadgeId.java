package com.service.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBadgeId implements Serializable {
    private Integer userId;
    private Integer badgeId;
}
