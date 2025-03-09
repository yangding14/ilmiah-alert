package com.ilmiah.ilmiah_alert.external.alert;

import com.ilmiah.ilmiah_alert.external.alert.dto.TelegramSendMessageReq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TelegramAlertClient implements AlertClient {
    private static final Logger logger = LoggerFactory.getLogger(TelegramAlertClient.class);
    private final RestClient restClient;
    private final String telegramBotToken;

    public TelegramAlertClient(
            @Value("${alert.telegram.telegram-bot-token}") String telegramBotToken) {
        this.restClient = RestClient.builder().build();
        this.telegramBotToken = telegramBotToken;
    }

    @Override
    public void sendAlert(String id, String message) {
        String uri =
                UriComponentsBuilder.fromUriString(
                                "https://api.telegram.org/bot{token}/sendMessage")
                        .buildAndExpand(telegramBotToken)
                        .toUriString();

        TelegramSendMessageReq requestBody = new TelegramSendMessageReq(id, message);

        try {
            restClient.post().uri(uri).body(requestBody).retrieve();
        } catch (Exception e) {
            logger.atError().setMessage("Failed to send alert to telegram").setCause(e).log();
        }
    }
}
