package com.ilmiah.ilmiah_alert.external.alert;

import com.ilmiah.ilmiah_alert.external.alert.dto.TelegramSendMessageReq;

import io.micrometer.observation.ObservationRegistry;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Component
public class TelegramAlertClient implements AlertClient {
    private static final Logger logger = LoggerFactory.getLogger(TelegramAlertClient.class);
    private static final int timeout = 2000;
    private final RestClient restClient;
    private final String telegramBotToken;

    public TelegramAlertClient(
            @Value("${alert.telegram.telegram-bot-token}") String telegramBotToken,
            ObservationRegistry observationRegistry) {
        this.restClient =
                RestClient.builder()
                        .requestFactory(getRequestFactory())
                        .observationRegistry(observationRegistry)
                        .build();
        this.telegramBotToken = telegramBotToken;
    }

    @Override
    public void sendAlert(String id, String message) {
        String uri =
                UriComponentsBuilder.fromUriString(
                                "https://api.telegram.org/bot{token}/sendMessage")
                        .build()
                        .toUriString();

        TelegramSendMessageReq requestBody = new TelegramSendMessageReq(id, message);

        try {
            restClient
                    .post()
                    .uri(uri, telegramBotToken)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.atError().setMessage("Failed to send alert to telegram").setCause(e).log();
        }
    }

    private HttpComponentsClientHttpRequestFactory getRequestFactory() {
        HttpClient httpClient =
                HttpClients.custom()
                        .setDefaultRequestConfig(
                                RequestConfig.custom()
                                        .setResponseTimeout(Timeout.of(Duration.ofMillis(timeout)))
                                        .build())
                        .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(timeout);
        return factory;
    }
}
