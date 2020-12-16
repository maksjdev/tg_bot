package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.information_filling;

import com.maks.graduate.work.tg_bot.service.api.TicketsPriceService;
import com.maks.graduate.work.tg_bot.service.messages.ReplyMessageService;
import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.maks.graduate.work.tg_bot.service.telegram_bot.TextConstants.ANSWER_NO;
import static com.maks.graduate.work.tg_bot.service.telegram_bot.TextConstants.ANSWER_YES;


@Component
public class SubscriptionHandler implements InputMessageHandler {

    private InMemoryCache inMemoryCache;
    private ReplyMessageService replyMessageService;
    private TicketsPriceService ticketsPriceService;

    public SubscriptionHandler(InMemoryCache inMemoryCache, ReplyMessageService replyMessageService, TicketsPriceService ticketsPriceService) {
        this.inMemoryCache = inMemoryCache;
        this.replyMessageService = replyMessageService;
        this.ticketsPriceService = ticketsPriceService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processInputMessage(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SUBSCRIPTION_FILLING;
    }

    private SendMessage processInputMessage(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButtonYes = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonNo = new InlineKeyboardButton();

        inlineKeyboardButtonYes.setText(ANSWER_YES);
        inlineKeyboardButtonYes.setCallbackData(ANSWER_YES);
        inlineKeyboardButtonNo.setText(ANSWER_NO);
        inlineKeyboardButtonNo.setCallbackData(ANSWER_NO);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButtonYes);
        keyboardButtonsRow2.add(inlineKeyboardButtonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        FilledInformation filledInformation = inMemoryCache.getFilledInformation(userId);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            filledInformation.setDepartureDate(LocalDate.parse(message.getText(), formatter));
        }catch (Exception e){
            inMemoryCache.setCurrentBotState(userId, BotState.DEPARTURE_DATE_FILLING);
            return replyMessageService.getReplyMessage(chatId, "reply.dateError");
        }

        callGetCheapest(filledInformation);

        inMemoryCache.setCurrentBotState(userId, BotState.SAVING_TO_DATABASE);
        return new SendMessage().setChatId(chatId).setText("Хочешь ли ты получать уведомления о изменениях цены на этот рейс?").setReplyMarkup(inlineKeyboardMarkup);
    }

    private void callGetCheapest(FilledInformation filledInformation) {
        try {
            ticketsPriceService.findCheapestTickets(filledInformation);
        }catch (IOException | URISyntaxException e){
            e.printStackTrace();
        }
    }
}
