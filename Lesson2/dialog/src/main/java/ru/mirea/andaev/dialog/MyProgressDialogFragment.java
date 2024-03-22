package ru.mirea.andaev.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressDialog.getProgress() <= progressDialog.getMax()){
                    try {
                        Thread.sleep(200);
                        progressDialog.incrementProgressBy(1);
                        if (progressDialog.getProgress() == progressDialog.getMax()){
                            progressDialog.dismiss();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        return progressDialog;
    }
}
