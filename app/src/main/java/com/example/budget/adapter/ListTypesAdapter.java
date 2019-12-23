package com.example.budget.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budget.R;
import com.example.budget.database.ExpenseType;

public class ListTypesAdapter extends RecyclerView.Adapter<ListTypesAdapter.ViewHolder> {


    public interface TypeDelegate{
        void chooseType(ExpenseType type);
        Drawable getDrawable();
    }

    private ExpenseType[] types = ExpenseType.values();
    private TypeDelegate delegate;
    private int selected = -1;
    private Drawable background;

    public ListTypesAdapter(TypeDelegate delegate) {
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public ListTypesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_type_rv_frag,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTypesAdapter.ViewHolder holder, int position) {
        holder.tvType.setText(types[position].toString());
        background = delegate.getDrawable();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.chooseType(types[position]);
                selected = position;
                holder.itemView.setBackground(background);
            }
        });
        if(selected==position){
            holder.itemView.setBackground(background);
        }else{
            holder.itemView.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return types.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type_rv_viewfrag);
        }
    }
}
