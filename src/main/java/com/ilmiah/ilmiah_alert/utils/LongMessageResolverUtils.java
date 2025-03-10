package com.ilmiah.ilmiah_alert.utils;

import java.util.ArrayList;
import java.util.List;

public class LongMessageResolverUtils {
    public List<String> breakMessageIntoParts(String longMessage, int maxMessageLength) {
        // check for \n and split the message into parts before hitting maxMessageLength
        List<String> messageParts = new ArrayList<>();

        if (longMessage == null || longMessage.isEmpty()) {
            return messageParts;
        }

        String[] lines = longMessage.split("\n");
        StringBuilder currentPart = new StringBuilder();

        for (String line : lines) {
            // If adding this line exceeds the max length and we already have content
            if (!currentPart.isEmpty()
                    && currentPart.length() + line.length() + 1 > maxMessageLength) {
                // Add current part to the list and start a new one
                messageParts.add(currentPart.toString());
                currentPart = new StringBuilder();
            }

            // If a single line is longer than max length, split it further
            if (line.length() > maxMessageLength) {
                // First add any existing content in currentPart
                if (!currentPart.isEmpty()) {
                    messageParts.add(currentPart.toString());
                    currentPart = new StringBuilder();
                }

                // Then break the long line into chunks
                for (int i = 0; i < line.length(); i += maxMessageLength) {
                    int endIndex = Math.min(i + maxMessageLength, line.length());
                    messageParts.add(line.substring(i, endIndex));
                }
            } else {
                // Add the line with a newline if not the first line in this part
                if (!currentPart.isEmpty()) {
                    currentPart.append("\n");
                }
                currentPart.append(line);
            }
        }

        // Don't forget to add the last part if it contains anything
        if (!currentPart.isEmpty()) {
            messageParts.add(currentPart.toString());
        }

        return messageParts;
    }
}
