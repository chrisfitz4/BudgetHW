package com.example.budget.presenter;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.budget.database.Expense;
import com.example.budget.database.ExpenseDatabase;
import com.example.budget.view.MainActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class ExpensePresenter implements Contract.ExpensePresenter {


    private ExpenseDatabase database;
    private List<Expense> expenses;
    private Contract.ExpenseView expenseView;
    private double cost;
    private double budgetMax;



////////////////Constructor///////////////////

    public ExpensePresenter(Contract.ExpenseView expenseView){
        this.expenseView = expenseView;
        try{
            new ASyncThread().execute();
        }catch (Exception e){
            Log.d("TAG_X", "Could not build database");
        }
    }



//////////read/update database//////////////

    //case 0: set up database and read its contents, then draw
    @Override
    public void getExpenses() {
        try {
            if (expenses.isEmpty()) {
                expenseView.noExpenses();
                cost = 0;
            } else {
                expenseView.displayExpenses(expenses);
                getTotalCost();
            }
        }catch(NullPointerException e){
            Log.d("TAG_X", "getExpenses: "+e);
        }
    }

    //case 1: add to the database and redraw
    @Override
    public void addExpense(Expense expense) {
        try{
            new ASyncThread1().execute(expense);
            getTotalCost();
        }catch (Exception e){
            Log.d("TAG_X", "Could not insert expense "+e);
        }
    }

    //case 2: update the database, reread, and redraw
    @Override
    public void updateExpense(Expense expense) {
        try{
            new ASyncThread2().execute(expense);
            getTotalCost();
        }catch (Exception e){
            Log.d("TAG_X", "Could not update expense");
        }
    }

    //case 3: delete from the database and redraw
    @Override
    public void deleteExpense(Expense expense) {
        try{
            new ASyncThread3().execute(expense);
            getTotalCost();
            expenses.remove(expense);
            expenseView.redraw(expenses);
        }catch (Exception e){
            Log.d("TAG_X", "Could not delete expense");
        }
    }

    //case 4: read from the database
    @Override
    public void getTotalCost() {
        try {
            Log.d("TAG_X", "getTotalCost: \n");
            new ASyncThread4().execute();
        }catch (Exception e){
            Log.d("TAG_X", "Could not get the total cost");
        }
    }



////////////////update/read non-database info//////////////
    @Override
    public void setBudgetMax(double budget) {
        this.budgetMax = budget;
        expenseView.displayBudget(budget);
        getPercentOfTotal();
    }

    @Override
    public void getPercentOfTotal() {
        try {
            Log.d("TAG_X", "getPercentOfTotal: "+budgetMax+", "+cost);
            expenseView.updateBackground(cost / budgetMax);
        }catch (ArithmeticException n){
            Log.d("TAG_X", "getPercentOfTotal: "+n);
        }
    }




////////////////background thread database processes///////////////

    //0: read database the first time and access its members
    private class ASyncThread extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            database = Room.databaseBuilder(((MainActivity) expenseView), ExpenseDatabase.class, "room.db").build();
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getExpenses();
        }
    }

    //1: add a member to the database
    private class ASyncThread1 extends AsyncTask<Expense,Void,Void>{

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().insertNewExpense(expense[0]);
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            expenseView.redraw(expenses);
        }
    }

    //2: update a member of the database
    private class ASyncThread2 extends AsyncTask<Expense,Void,Void>{

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().updateExpense(expense[0]);
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            expenseView.redraw(expenses);
        }
    }

    //3: delete a member from the database
    private class ASyncThread3 extends AsyncTask<Expense,Void,Void>{

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().removeExpense(expense[0]);
            return null;
        }
    }

    //4: read the total cost from the database
    private class ASyncThread4 extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            cost = database.ExpenseDAO().getTotalCost();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getPercentOfTotal();
        }
    }
}
