package com.maks.graduate.work.tg_bot.model;

import com.maks.graduate.work.tg_bot.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
        Аудит поля
    */
    @CreatedDate
    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updated;
}
