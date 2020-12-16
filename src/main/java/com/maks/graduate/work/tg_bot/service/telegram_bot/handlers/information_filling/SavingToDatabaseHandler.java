package com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.information_filling;

import com.maks.graduate.work.tg_bot.model.Route;
import com.maks.graduate.work.tg_bot.repository.RouteRepository;
import com.maks.graduate.work.tg_bot.service.messages.ReplyMessageService;
import com.maks.graduate.work.tg_bot.service.telegram_bot.cache.InMemoryCache;
import com.maks.graduate.work.tg_bot.service.telegram_bot.enums.BotState;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import com.maks.graduate.work.tg_bot.service.telegram_bot.handlers.CallbackQueryHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.maks.graduate.work.tg_bot.service.telegram_bot.TextConstants.ANSWER_YES;


@Component
public class SavingToDatabaseHandler implements CallbackQueryHandler {

    private InMemoryCache inMemoryCache;
    private ReplyMessageService replyMessageService;
    private RouteRepository routeRepository;

    public SavingToDatabaseHandler(InMemoryCache inMemoryCache, ReplyMessageService replyMessageService, RouteRepository routeRepository) {
        this.inMemoryCache = inMemoryCache;
        this.replyMessageService = replyMessageService;
        this.routeRepository = routeRepository;
    }

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        return processCallbackQuery(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SAVING_TO_DATABASE;
    }

    private SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
        SendMessage replyToUser = null;

        if (callbackQuery.getData().equals(ANSWER_YES)){
            FilledInformation filledInformation = inMemoryCache.getFilledInformation(userId);
            saveFilledInformation(userId, chatId, filledInformation);
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.subscriptionCreated");
        }else replyToUser = replyMessageService.getReplyMessage(chatId, "reply.thanks");

        inMemoryCache.setCurrentBotState(userId, BotState.SAY_HELLO);

        return replyToUser;
    }

    private void saveFilledInformation(int userId, long chatId, FilledInformation filledInformation) {
        Route route = new Route();
        route.setUser_id(userId);
        route.setChat_id(chatId);
        route.setDeparturePlace(filledInformation.getDeparturePlace());
        route.setArrivalPlace(filledInformation.getArrivalPlace());
        route.setDepartureDate(filledInformation.getDepartureDate());
        route.setIATAOrigin(filledInformation.getIATAOrigin());
        route.setIATADestination(filledInformation.getIATADestination());
        route.setIsNotificationEnabled(true);
        route.setPrice(100);
        routeRepository.save(route);
    }

}
