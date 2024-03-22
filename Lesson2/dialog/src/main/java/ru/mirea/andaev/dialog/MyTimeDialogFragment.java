package ru.mirea.andaev.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getActivity().getApplicationContext(), "Время установлено!",Toast.LENGTH_SHORT).show();
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeSetListener, 12, 0, true);

        timePickerDialog.setTitle("Выбор времени:");

        return timePickerDialog;
    }
}
