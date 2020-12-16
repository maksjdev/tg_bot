package com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilledInformation {
    String departurePlace;
    String arrivalPlace;
    LocalDate departureDate;
    boolean getNotifications;
    String IATAOrigin;
    String IATADestination;
}
