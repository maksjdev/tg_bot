package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.information_filling;

import com.maks.graduate.work.tg_bot.service.messages.ReplyMessageService;
import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class DeparturePlaceHandler implements InputMessageHandler {

    private InMemoryCache inMemoryCache;
    private ReplyMessageService replyMessageService;

    public DeparturePlaceHandler(InMemoryCache inMemoryCache, ReplyMessageService replyMessageService) {
        this.inMemoryCache = inMemoryCache;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processInputMessage(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEPARTURE_PLACE_FILLING;
    }

    private SendMessage processInputMessage(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askDeparturePlace");
        inMemoryCache.setCurrentBotState(userId, BotState.ARRIVAL_PLACE_FILLING);

        return replyToUser;
    }

}
