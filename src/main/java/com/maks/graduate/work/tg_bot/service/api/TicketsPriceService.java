package com.maks.graduate.work.tg_bot.service.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.maks.graduate.work.tg_bot.model.api_models.TicketPrice;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class TicketsPriceService {

    private static String TICKET_PRICES_URL = "http://min-prices.aviasales.ru/calendar_preload?";

    public void findCheapestTickets(FilledInformation filledInformation) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(TICKET_PRICES_URL);
        builder.addParameter("origin_iata", filledInformation.getIATAOrigin());
        builder.addParameter("destination",  filledInformation.getIATADestination());
        URL url = new URL(builder.build().toString().replaceAll("%22", ""));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        InputStream is = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonMap = mapper.readTree(is);
        ArrayNode arrayNode = (ArrayNode) jsonMap.path("current_depart_date_prices");
        List<TicketPrice> pojos = mapper.readValue(arrayNode.toString(), new TypeReference<List<TicketPrice>>() {});
    }

}
