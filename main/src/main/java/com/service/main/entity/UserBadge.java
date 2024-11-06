package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBadge {
    @EmbeddedId
    private UserBadgeId userBadgeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    @MapsId("badgeId")
    @JsonBackReference
    private Badge badge;

    private boolean isShow;

    private Date createdAt = new Date();


}

