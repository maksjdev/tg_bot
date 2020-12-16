package com.maks.graduate.work.tg_bot.service.telegram_bot;

import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.CallbackQueryHandler;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BotStateContext {

    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();
    private Map<BotState, CallbackQueryHandler> callbackQueryHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers, List<CallbackQueryHandler> callbackQueryHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
        callbackQueryHandlers.forEach(handler -> this.callbackQueryHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler inputMessageHandler = findMessageHandler(currentState);
        return inputMessageHandler.handle(message);
    }

    public SendMessage processCallbackQuery(BotState currentState, CallbackQuery callbackQuery) {
        CallbackQueryHandler callbackQueryHandler = findCallbackQueryHandler(currentState);
        return callbackQueryHandler.handle(callbackQuery);
    }

    private InputMessageHandler findMessageHandler(BotState currentState){
        return messageHandlers.get(currentState);
    }


    private CallbackQueryHandler findCallbackQueryHandler(BotState currentState){
        return callbackQueryHandlers.get(currentState);
    }
}
