package com.ilmiah.ilmiah_alert.external.alert.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TelegramSendMessageReq(String chatId, String text) {}
