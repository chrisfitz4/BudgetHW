package com.example.budget.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import butterknife.internal.ListenerClass;

public class ViewExpenseFrag extends Fragment implements ListTypesAdapter.TypeDelegate{


    @BindView(R.id.et_type_viewfrag)
    EditText etType;
    @BindView(R.id.et_location_viewfrag)
    EditText etLocation;
    @BindView(R.id.et_cost_viewfrag)
    EditText etCost;
    @BindView(R.id.et_month_viewfrag)
    EditText etMonth;
    @BindView(R.id.et_day_viewfrag)
    EditText etDay;
    @BindView(R.id.et_year_viewfrag)
    EditText etYear;
    @BindView(R.id.bt_cancel_viewfrag)
    Button btCancel;
    @BindView(R.id.bt_confirm_viewfrag)
    Button btConfirm;
    @BindView(R.id.rv_types_viewfrag)
    RecyclerView rvTypes;

    private Expense expense = null;
    private ExpenseType type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_member,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this,view);

        rvTypes.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        rvTypes.setAdapter(new ListTypesAdapter(this));
        rvTypes.setVisibility(View.INVISIBLE);

        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = getArguments();
        try {
            expense = (Expense)(savedInstanceState.getParcelable(Constants.GET_SELECTED_EXPENSE_KEY));
            type = ExpenseType.valueOf(expense.getType());
        }catch (NullPointerException n){
            Log.d("TAG_X", "onViewCreated: "+n);
        }
        if(expense==null){
            getFragmentManager().popBackStack();
        }else {
            setTexts();
            setClickListeners();
        }


    }


////////////////set up view with text and listeners///////

    private void setTexts(){
        etType.setText(expense.getType());
        etType.setInputType(0);

        etLocation.setText(expense.getLocation());
        etLocation.setInputType(0);

        etCost.setText(""+expense.getCost());
        etCost.setInputType(0);

        String[] date = expense.getDate().split("/");
        etMonth.setText(date[0]);
        etMonth.setInputType(0);
        etDay.setText(date[1]);
        etDay.setInputType(0);
        etYear.setText(date[2]);
        etYear.setInputType(0);
    }

    private void setClickListeners(){
        etType.setOnLongClickListener(new LongClickType());
        etLocation.setOnLongClickListener(new LongClickText());
        etMonth.setOnLongClickListener(new LongClickNum());
        etDay.setOnLongClickListener(new LongClickNum());
        etYear.setOnLongClickListener(new LongClickNum());
        etCost.setOnLongClickListener(new LongClickDec());
        btCancel.setOnClickListener(new CancelButton());
        btConfirm.setOnClickListener(new ConfirmButton());
    }




//////////////click listeners///////////////////
    private class LongClickText implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setInputType(1);
            ((EditText)v).setText("");
            return true;
        }
    }

    private class LongClickType implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setVisibility(View.INVISIBLE);
            rvTypes.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private class LongClickNum implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setInputType(2);
            ((EditText)v).setText("");
            return true;
        }
    }

    private class LongClickDec implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setText("");
            ((EditText)v).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return true;
        }
    }

    private class CancelButton implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    }

    private class ConfirmButton implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Constants.VIEW_EXPENSE_ACTION);
            intent.setAction(Constants.VIEW_EXPENSE_ACTION);
            intent.putExtra(Constants.GET_SELECTED_EXPENSE_KEY,getExpense());
            getContext().sendBroadcast(intent);

            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void chooseType(ExpenseType etype) {
        this.type = etype;
        etType.setVisibility(View.VISIBLE);
        etType.setText(type.toString());
        rvTypes.setVisibility(View.INVISIBLE);
    }


    @Override
    public Drawable getDrawable() {
        return this.getResources().getDrawable(R.drawable.background_selected_type);
    }

    ////////////get the values of the new expense///////
    private Expense getExpense(){
        Expense newExpense = new Expense();

        String date = "";
        date+=etMonth.getText().toString();
        date+="/";
        date+=etDay.getText().toString();
        date+="/";
        date+=etYear.getText().toString();

        newExpense.setKey(expense.getKey());
        newExpense.setType(type);
        newExpense.setLocation(etLocation.getText().toString());
        newExpense.setCost(Double.parseDouble(etCost.getText().toString()));
        newExpense.setDate(date);

        return newExpense;
    }

}
