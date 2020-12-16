package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.information_filling;

import com.maks.graduate.work.tg_bot.service.api.IATACodesService;
import com.maks.graduate.work.tg_bot.service.messages.ReplyMessageService;
import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;


@Component
public class DepartureDateHandler implements InputMessageHandler {

    private InMemoryCache inMemoryCache;
    private ReplyMessageService replyMessageService;
    private IATACodesService iataCodesService;

    public DepartureDateHandler(InMemoryCache inMemoryCache, ReplyMessageService replyMessageService, IATACodesService iataCodesService) {
        this.inMemoryCache = inMemoryCache;
        this.replyMessageService = replyMessageService;
        this.iataCodesService = iataCodesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processInputMessage(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEPARTURE_DATE_FILLING;
    }

    private SendMessage processInputMessage(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        FilledInformation filledInformation = inMemoryCache.getFilledInformation(userId);
        filledInformation.setArrivalPlace(message.getText());
        inMemoryCache.setFilledInformation(userId, filledInformation);

        try {
           filledInformation = iataCodesService.fillIATACodes(filledInformation);
        } catch (IOException e) {
            inMemoryCache.setCurrentBotState(userId, BotState.SAY_HELLO);
            return replyMessageService.getReplyMessage(chatId, "reply.error");
        }
        if (filledInformation.getIATAOrigin() == null || filledInformation.getIATADestination() == null){
            inMemoryCache.setCurrentBotState(userId, BotState.ARRIVAL_PLACE_FILLING);
            return replyMessageService.getReplyMessage(chatId, "reply.iataError");
        }

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askDepartureDate");
        inMemoryCache.setCurrentBotState(userId, BotState.SUBSCRIPTION_FILLING);

        return replyToUser;
    }

}
