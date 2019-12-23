package com.example.budget.database;

public enum ExpenseType {


    FOOD ("FOOD"),
    RENT ("RENT"),
    TRANSPORTATION ("TRANSPORTATION"),
    UTILITIES ("UTILITIES"),
    MEDICAL ("MEDICAL"),
    RECREATION ("RECREATION"),
    OTHER ("OTHER");


    private final String type;

    ExpenseType(String type) {
        this.type = type;
    }


}
