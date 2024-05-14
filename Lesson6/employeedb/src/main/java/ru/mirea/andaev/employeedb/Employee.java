package ru.mirea.andaev.employeedb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int salary;
    public Employee(long id, String name, int salary){
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @NonNull
    @Override
    public String toString(){
        return String.format(Locale.getDefault(),"id: %d, name: %s, salary: %d",id,name,salary);
    }
}
