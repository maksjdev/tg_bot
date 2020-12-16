package com.maks.graduate.work.tg_bot.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maks.graduate.work.tg_bot.service.telegram_bot.filled_information.FilledInformation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 *  Сервис для получения IATA кодов для городов.
 *  IATA - International Air Transport Association
 */
@Component
public class IATACodesService {

    private static String IATA_CODES_URL = "https://www.travelpayouts.com/widgets_suggest_params?q=";

    public FilledInformation fillIATACodes(FilledInformation filledInformation) throws IOException {
        String keyword = "Из " + filledInformation.getDeparturePlace() + " в " + filledInformation.getArrivalPlace();
        IATA_CODES_URL += URLEncoder.encode(keyword, "UTF-8");
        URL url = new URL(IATA_CODES_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        InputStream is = connection.getInputStream();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonMap = mapper.readTree(is);

        if (jsonMap.get("origin") != null && jsonMap.get("destination") != null) {
            filledInformation.setIATAOrigin(jsonMap.get("origin").get("iata").toString());
            filledInformation.setIATADestination(jsonMap.get("destination").get("iata").toString());
        }
        return filledInformation;
    }
}
