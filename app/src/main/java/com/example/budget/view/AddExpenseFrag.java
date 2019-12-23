package com.example.budget.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budget.R;
import com.example.budget.adapter.ListTypesAdapter;
import com.example.budget.database.Expense;
import com.example.budget.database.ExpenseType;
import com.example.budget.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExpenseFrag extends Fragment implements ListTypesAdapter.TypeDelegate {

    @BindView(R.id.rv_type_addfrag)
    RecyclerView type;
    @BindView(R.id.et_location_addfrag)
    EditText location;
    @BindView(R.id.et_month_addfrag)
    EditText month;
    @BindView(R.id.et_day_addfrag)
    EditText day;
    @BindView(R.id.et_year_addfrag)
    EditText year;
    @BindView(R.id.et_cost_addfrag)
    EditText cost;
    @BindView(R.id.bt_save_addfrag)
    Button save;

    private ExpenseType expenseType = ExpenseType.OTHER;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_member,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        type.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        type.setAdapter(new ListTypesAdapter(this));
    }



/////////////oncClick//////////////////////

    @OnClick(R.id.bt_save_addfrag)
    public void onClick(View view){

        String expenseLocation = location.getText().toString().trim();
        String expenseMonth = month.getText().toString().trim();
        String expenseDay = day.getText().toString().trim();
        String expenseYear = year.getText().toString().trim();
        double expenseCost = 0;
        boolean allGood = true;
        //check the cost
        try{
            expenseCost = Double.parseDouble(cost.getText().toString().trim());
        }catch (NumberFormatException e){
            Log.d("TAG_X", "Could not parse double "+expenseCost+", "+e);
            Toast.makeText(this.getContext(),"Input a valid cost.",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            allGood = false;
        }
        //check the month
        try {
            double monthNum = Double.parseDouble(expenseMonth);
            if (monthNum > 12||monthNum==0) {
                Toast.makeText(getContext(), "Input a valid month", Toast.LENGTH_SHORT).show();
                allGood = false;
            }
        }catch (NumberFormatException e) {
            allGood = false;
            Toast.makeText(getContext(), "Input a valid month", Toast.LENGTH_SHORT).show();
        }
        //check the day
        try {
            double dayNum = Double.parseDouble(expenseDay);
            if(dayNum>31||dayNum==0) {
                Toast.makeText(getContext(), "Input a valid day", Toast.LENGTH_SHORT).show();
                allGood = false;
            }
        }catch (NumberFormatException e){
            Toast.makeText(getContext(), "Input a valid day", Toast.LENGTH_SHORT).show();
            allGood = false;
        }
        //check the year
        try {
            if(expenseYear.equals("")){
                Toast.makeText(getContext(),"Input a year",Toast.LENGTH_SHORT).show();
                allGood = false;
            }
        }catch (NumberFormatException e){
            Toast.makeText(getContext(),"Input a year",Toast.LENGTH_SHORT).show();
            allGood = false;
        }
        if(allGood) {
            Expense expense = new Expense(expenseLocation, expenseType, expenseCost, expenseMonth + '/' + expenseDay + '/' + expenseYear);
            deleteAll();
            broadcast(expense);
        }
    }

    @OnClick(R.id.bt_cancel_addfrag)
    public void onCancel(View view){
        deleteAll();
        getFragmentManager().popBackStack();
    }

    private void deleteAll(){
        location.setText("");
        month.setText("");
        day.setText("");
        year.setText("");
        cost.setText("");
    }


/////////////////////
    private void broadcast(Expense expense){
        //broadcast the expense to the parent activity (main)
        Intent intent = new Intent(Constants.ADD_EXPENSE_ACTION);
        intent.setAction(Constants.ADD_EXPENSE_ACTION);
        intent.putExtra(Constants.GET_ADDED_EXPENSE_KEY,expense);
        getContext().sendBroadcast(intent);

        //end the fragment
        getFragmentManager().popBackStack();

        Log.d("TAG_X", "onClick: "+expense);
    }


    @Override
    public void chooseType(ExpenseType type) {
        this.expenseType = type;
    }

    @Override
    public Drawable getDrawable() {
        return this.getResources().getDrawable(R.drawable.background_selected_type);
    }
}
