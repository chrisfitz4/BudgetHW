package com.example.budget.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = Expense.class)
public abstract class ExpenseDatabase extends RoomDatabase {

    public abstract ExpenseDAO ExpenseDAO();

}
