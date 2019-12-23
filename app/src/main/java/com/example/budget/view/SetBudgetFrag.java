package com.example.budget.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.util.DBUtil;

import com.example.budget.R;
import com.example.budget.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetBudgetFrag extends Fragment {

    @BindView(R.id.bt_cancel_setfrag)
    Button cancel;
    @BindView(R.id.bt_confirm_setfrag)
    Button confirm;
    @BindView(R.id.et_setbudg_setfrag)
    EditText budget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_budget,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double doubBudget = Double.parseDouble(budget.getText().toString().trim());
                    doubBudget = Math.round(doubBudget*100)/100.0;
                    broadcast(doubBudget);
                    getFragmentManager().popBackStack();
                }catch (Exception e){
                    Log.d("TAG_X", "onClick: "+e);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget.setText("");
                getFragmentManager().popBackStack();
            }
        });
    }


    private void broadcast(double budgetNum){
        Intent intent = new Intent(Constants.SET_BUDGET_ACTION);
        intent.setAction(Constants.SET_BUDGET_ACTION);
        intent.putExtra(Constants.SET_BUDGET_KEY,budgetNum);
        getContext().sendBroadcast(intent);
    }
}
