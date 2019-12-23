package com.example.budget.presenter;

import com.example.budget.database.Expense;

import java.util.List;

public interface Contract {

    interface ExpensePresenter{

        void setBudgetMax(double n);
        void getExpenses();
        void updateExpense(Expense expense);
        void addExpense(Expense expense);
        void deleteExpense(Expense expense);
        void getTotalCost();
        void getPercentOfTotal();
    }


    interface ExpenseView{

        void displayExpenses(List<Expense> expenses);
        void updateBackground(double cost);
        void noExpenses();
        void redraw(List<Expense> expenses);
        void displayBudget(double budget);
    }

}
