package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.start_handler;

import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.InputMessageHandler;
import com.maks.graduate.work.tg_bot.service.messages.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartMessageHandler implements InputMessageHandler {

    private InMemoryCache userDataCache;
    private ReplyMessageService replyMessageService;


    public StartMessageHandler(InMemoryCache userDataCache, ReplyMessageService replyMessageService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processInputMessage(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SAY_HELLO;
    }

    private SendMessage processInputMessage(Message message) {
        long chatId = message.getChatId();
        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.sayHello");
        return replyToUser;
    }
}
