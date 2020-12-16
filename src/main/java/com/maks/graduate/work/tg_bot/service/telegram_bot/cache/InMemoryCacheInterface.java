package com.maks.graduate.work.tg_bot.service.telegram_bot.cache;

import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;

public interface InMemoryCacheInterface {

    void setCurrentBotState(int userId, BotState state);

    BotState getCurrentBotState(int userId);

    void setFilledInformation(int userId, FilledInformation userData);

    FilledInformation getFilledInformation(int userId);

//    void saveUserData(int userId, FilledInformation data);
}
