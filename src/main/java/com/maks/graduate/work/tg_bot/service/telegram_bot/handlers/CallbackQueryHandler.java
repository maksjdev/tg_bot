package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers;

import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 *  Callback queries from user.
 */
public interface CallbackQueryHandler {
    SendMessage handle(CallbackQuery callbackData);

    BotState getHandlerName();

}
