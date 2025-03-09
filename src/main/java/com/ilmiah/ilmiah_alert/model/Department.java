package com.ilmiah.ilmiah_alert.model;

public enum Department {
    SOFTWARE_ENGINEERING(1),
    COMPUTER_SYSTEM_AND_NETWORK(2),
    MULTIMEDIA(3),
    ARTIFICIAL_INTELLIGENCE(4),
    INFORMATION_SYSTEM(5),
    ISLAMIC_STUDIES(6);

    private final int id;

    Department(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
