package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers;

import com.maks.graduate.work.tg_bot.service.telegram_bot.BotStateContext;
import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UserMessageHandler {

    private static final String START = "/start";
    private static final String FIND = "/find";
    private static final String HELP = "/help";

    private BotStateContext botStateContext;
    private InMemoryCache inMemoryCache;

    public UserMessageHandler(BotStateContext botStateContext, InMemoryCache userDataCached) {
        this.botStateContext = botStateContext;
        inMemoryCache = userDataCached;
    }

    public SendMessage handleUpdate(Update update){
        SendMessage replyMessage = null;
        if (update.hasCallbackQuery()) replyMessage = handleCallbackQuery(update.getCallbackQuery());
        else {
            Message message = update.getMessage();
            if (message != null && message.hasText()){
                log.info("New message from User: {}, chatId: {}, message text: {}", message.getFrom(), message.getChatId(), message.getText());
                replyMessage = handleInputMessage(message);
            }
        }
        return replyMessage;
    }

    private SendMessage handleCallbackQuery(CallbackQuery callbackQuery){
        int userId = callbackQuery.getFrom().getId();

        return  botStateContext.processCallbackQuery(inMemoryCache.getCurrentBotState(userId), callbackQuery);

    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage){
            case START:
                botState = BotState.SAY_HELLO;
                break;
            case FIND:
                botState = BotState.DEPARTURE_PLACE_FILLING;
                break;
            case HELP:
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = inMemoryCache.getCurrentBotState(userId);
                break;
        }
        inMemoryCache.setCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }


}
