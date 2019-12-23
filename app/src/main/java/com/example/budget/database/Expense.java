package com.example.budget.database;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "expenses")
public class Expense implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int key;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "cost")
    private double cost;
    @ColumnInfo(name = "date")
    private String date;



/////////////constructors//////////////



    public Expense(int key, String location, String type, double cost, String date) {
        this.key = key;
        this.location = location;
        this.type = type;
        this.cost = cost;
        this.date = date;
    }

    @Ignore
    public Expense(int key, String location, ExpenseType type, double cost, String date) {
        this.key = key;
        this.location = location;
        this.type = type.toString();
        this.cost = cost;
        this.date = date;
    }

    @Ignore
    public Expense(String location, ExpenseType type, double cost, String date) {
        this.location = location;
        this.type = type.toString();
        this.cost = cost;
        this.date = date;
    }

    @Ignore
    public Expense(){
        this.key = -1;
        this.location = "";
        this.type = "OTHER";
        this.cost = 0;
        this.date = "";
    }



///////////////toString/////////

    protected Expense(Parcel in) {
        key = in.readInt();
        location = in.readString();
        type = in.readString();
        cost = in.readDouble();
        date = in.readString();
    }

    public String toString(){
        String toReturn = type+" expense for $%.2f at "+location+" on "+date;
        return String.format(toReturn,cost);
    }



///////////////getters and setters///////


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ExpenseType getTypeExpense() {
        return ExpenseType.valueOf(type);
    }

    public void setTypeExpense(ExpenseType type) {
        this.type = type.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType(ExpenseType type) {
        this.type = type.toString();
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



/////////////Parcelable implementation////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(location);
        dest.writeString(type);
        dest.writeDouble(cost);
        dest.writeString(date);
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };



}
