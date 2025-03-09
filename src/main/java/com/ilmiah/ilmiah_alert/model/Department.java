package com.ilmiah.ilmiah_alert.model;

public enum Department {
    SOFTWARE_ENGINEERING(1, "Software Engineering"),
    COMPUTER_SYSTEM_AND_NETWORK(2, "Computer System and Network"),
    MULTIMEDIA(3, "Multimedia"),
    ARTIFICIAL_INTELLIGENCE(4, "Artificial Intelligence"),
    INFORMATION_SYSTEM(5, "Information System"),
    ISLAMIC_STUDIES(6, "Islamic Studies"),
    ;

    private final int id;
    private final String departmentName;

    Department(int id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public int id() {
        return id;
    }

    public String departmentName() {
        return departmentName;
    }
}
