package com.example.budget.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budget.R;
import com.example.budget.adapter.ListExpensesAdapter;
import com.example.budget.database.Expense;
import com.example.budget.presenter.Contract;
import com.example.budget.presenter.ExpensePresenter;
import com.example.budget.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Contract.ExpenseView, ListExpensesAdapter.SelectRVDelegate {

    @BindView(R.id.background_percent)
    ImageView backgroundView;
    @BindView(R.id.bt_addExpense_main)
    Button addExpenseButton;
    @BindView(R.id.layout_main)
    ConstraintLayout layout;
    @BindView(R.id.rv_expenses_main)
    RecyclerView recyclerViewExpenses;
    @BindView(R.id.bt_budget_main)
    Button budget;


    Contract.ExpensePresenter presenter;
    SharedPreferences sharedPreferences;


/////////////receivers/////////////////////

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Expense expense = intent.getParcelableExtra(Constants.GET_ADDED_EXPENSE_KEY);
            presenter.addExpense(expense);
            Log.d("TAG_X", "onReceive: "+expense);
        }
    };

    BroadcastReceiver receiverTwo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Expense expense = intent.getParcelableExtra(Constants.GET_SELECTED_EXPENSE_KEY);
            presenter.updateExpense(expense);
            Log.d("TAG_X", "onReceiveTwo: "+expense);
        }
    };

    BroadcastReceiver receiverThree = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double budgetNum = intent.getDoubleExtra(Constants.SET_BUDGET_KEY,0);
            presenter.setBudgetMax(budgetNum);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(Constants.GET_MY_BUDGET_SP_KEY,(float)budgetNum);
            editor.commit();
            editor.clear();
            Log.d("TAG_X", "onReceiveThree: "+budgetNum);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        this.unregisterReceiver(receiverTwo);
        this.unregisterReceiver(receiverThree);
    }



//////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new ExpensePresenter(this);
        presenter.getExpenses();

        sharedPreferences = getSharedPreferences(Constants.GET_SHARED_PREFERENCES,MODE_PRIVATE);
        float my_budget = sharedPreferences.getFloat(Constants.GET_MY_BUDGET_SP_KEY,0);
        if(my_budget==0){
            Toast.makeText(this, "Please input a budget.", Toast.LENGTH_SHORT).show();
        }else{
            presenter.setBudgetMax(my_budget);
        }

        this.registerReceiver(receiver, new IntentFilter(Constants.ADD_EXPENSE_ACTION));
        this.registerReceiver(receiverTwo, new IntentFilter(Constants.VIEW_EXPENSE_ACTION));
        this.registerReceiver(receiverThree, new IntentFilter(Constants.SET_BUDGET_ACTION));
    }


    @Override
    public void displayExpenses(List<Expense> expenses) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);

        recyclerViewExpenses.setLayoutManager(manager);
        recyclerViewExpenses.setAdapter(new ListExpensesAdapter(expenses,this));
        recyclerViewExpenses.addItemDecoration(decoration);
    }

    @Override
    public void displayBudget(double budgetNum) {
        budget.setText(String.format("$%.2f",budgetNum));
    }

    @Override
    public void updateBackground(double costPercent) {
        int height = layout.getHeight();
        int backgroundHeight = (int)(costPercent*height);
        backgroundView.setMinimumHeight(backgroundHeight);
        int color = chooseColor(costPercent);
        backgroundView.setBackgroundColor(color);
        Log.d("TAG_X", "updateBackgroundHeight: "+backgroundHeight+", "+height+", "+costPercent);
        Log.d("TAG_X", "updateBackgroundColor: "+color);
    }

    public int chooseColor(double percent){
        double b = 1.0/2+Math.sqrt(5)/2;
        double c = Math.sqrt(5)/2-1.0/2;
        int red = (int)(255*(b-1/(percent+c)));
        int green = (int)(255*(b+1/(percent-b)));
        return Color.rgb(red,green,0);
    }

    @Override
    public void noExpenses() {

    }

    @Override
    public void redraw(List<Expense> expenses) {
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpenses.setAdapter(new ListExpensesAdapter(expenses,this));
    }



////////////click listeners/////////////

    @OnClick(R.id.bt_addExpense_main)
    public void onClick(View view){
        AddExpenseFrag addExpenseFrag = new AddExpenseFrag();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frame_main,addExpenseFrag).
                addToBackStack(addExpenseFrag.getTag()).
                commit();
    }


    //called when the RV is clicked
    @Override
    public void startNewFragment(Expense expense) {
        ViewExpenseFrag viewExpenseFrag = new ViewExpenseFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.GET_SELECTED_EXPENSE_KEY,expense);

        viewExpenseFrag.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main,viewExpenseFrag)
                .addToBackStack(viewExpenseFrag.getTag())
                .commit();
    }

    @OnClick(R.id.bt_budget_main)
    public void onClickBudget(View v) {
        SetBudgetFrag budgetFrag = new SetBudgetFrag();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main, budgetFrag)
                .addToBackStack(budgetFrag.getTag())
                .commit();
    }

    public void deleteText(View view){
        ((EditText)view).setText("");
    }

}
