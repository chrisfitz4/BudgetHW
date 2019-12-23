package com.example.budget.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDAO {

    @Query("SELECT * FROM expenses")
    List<Expense> getAllExpenses();

    @Insert
    void insertNewExpense(Expense expense);

    @Delete
    void removeExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Query("SELECT SUM(cost) FROM expenses")
    double getTotalCost();
}
