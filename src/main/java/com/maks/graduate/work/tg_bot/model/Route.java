package com.maks.graduate.work.tg_bot.model;


import com.maks.graduate.work.tg_bot.converters.LocalDateConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "routes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Route extends BaseEntity{

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "chat_id")
    private long chat_id;

    @Column(name = "departure_place")
    private String departurePlace;

    @Column(name = "arrival_place")
    private String arrivalPlace;

    @Column(name = "departure_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate departureDate;

    @Column(name = "notification_enabled")
    private Boolean isNotificationEnabled = false;

    @Column(name = "iata_origin")
    private String IATAOrigin;

    @Column(name = "iata_destination")
    private String IATADestination;

    @Column(name = "price")
    private int price;
}