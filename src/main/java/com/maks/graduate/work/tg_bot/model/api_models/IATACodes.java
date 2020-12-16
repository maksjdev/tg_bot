package com.maks.graduate.work.tg_bot.model.api_models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IATACodes {
    public String originIATA;
    public String destinationIATA;
}
