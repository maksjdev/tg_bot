package com.maks.graduate.work.tg_bot.service.telegram_bot.cache;


import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryCache implements InMemoryCacheInterface {

    /**
     * In-memory cache для сохранения информации от юзера
     * и сохранение состояния бота между реквестами.
     */
    private Map<Integer, BotState> botStateMap = new HashMap<>();
    private Map<Integer, FilledInformation> filledInformationMap = new HashMap<>();

    @Override
    public void setFilledInformation(int userId, FilledInformation userData) {
        filledInformationMap.put(userId, userData);
    }

    @Override
    public void setCurrentBotState(int userId, BotState state) {
        botStateMap.put(userId, state);
    }

    @Override
    public BotState getCurrentBotState(int userId) {
        BotState botState = botStateMap.get(userId);
        if (botState == null) botState = BotState.SAY_HELLO;
        return botState;
    }

    @Override
    public FilledInformation getFilledInformation(int userId) {
        FilledInformation filledInformation = filledInformationMap.get(userId);
        if (filledInformation == null) filledInformation = new FilledInformation();
        return filledInformation;
    }
}
