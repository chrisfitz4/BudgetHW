package com.example.budget.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budget.R;
import com.example.budget.database.Expense;
import com.example.budget.util.Constants;

import java.util.List;

import butterknife.ButterKnife;

public class ListExpensesAdapter extends RecyclerView.Adapter<ListExpensesAdapter.ViewHolder> {

    public interface SelectRVDelegate{
        void startNewFragment(Expense expense);
    }

    List<Expense> expenses;
    SelectRVDelegate delegate;


    public ListExpensesAdapter(List<Expense> expenses, SelectRVDelegate delegate) {
        this.expenses = expenses;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_expenses_rv_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.location.setText(expense.getLocation());
        holder.date.setText(expense.getDate());
        holder.cost.setText(String.format("$%.2f",expense.getCost()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.startNewFragment(expense);
            }
        });
    }



    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        TextView date;
        TextView cost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.tv_location_rv_main);
            date = itemView.findViewById(R.id.tv_date_rv_main);
            cost = itemView.findViewById(R.id.tv_cost_rv_main);
        }
    }
}
