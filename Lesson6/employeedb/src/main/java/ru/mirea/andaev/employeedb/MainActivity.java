package ru.mirea.andaev.employeedb;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDataBase dataBase = App.getInstance().getDataBase();
        EmployeeDao dao = dataBase.employeeDao();
        try{
            dao.insert(new Employee(1,"Марина",150000));
            dao.insert(new Employee(2,"Сева",75000));
        }
        catch (RuntimeException e){
            Log.d(getLocalClassName(), "Значение уже есть в БД");
        }
        finally {
            List<Employee> employees = dao.getAll();
            for (Employee e: employees){
                Log.d(getLocalClassName(),e.toString());
            }
        }
    }
}