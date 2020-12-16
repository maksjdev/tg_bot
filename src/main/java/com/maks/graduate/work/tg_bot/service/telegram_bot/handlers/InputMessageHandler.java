package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers;


import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 *  Input messages from user handler
 */
public interface InputMessageHandler {

    SendMessage handle(Message message);

    BotState getHandlerName();
}
