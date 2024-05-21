package ru.mirea.andaev.mireaproject.ui.lesson7;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.andaev.mireaproject.R;
import ru.mirea.andaev.mireaproject.databinding.FragmentBoredapiBinding;
import ru.mirea.andaev.mireaproject.ui.lesson7.api.BoredResponse;
import ru.mirea.andaev.mireaproject.ui.lesson7.api.BoredService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoredapiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoredapiFragment extends Fragment {
    FragmentBoredapiBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean is_permissions_granted = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BoredapiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoredapiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoredapiFragment newInstance(String param1, String param2) {
        BoredapiFragment fragment = new BoredapiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        checkAndRequestPermissions();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBoredapiBinding.inflate(inflater,container,false);
        binding.boredButton.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.boredapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            BoredService service = retrofit.create(BoredService.class);
            Call<BoredResponse> call = service.getActivityToDo();
            call.enqueue(new retrofit2.Callback<BoredResponse>() {
                @Override
                public void onResponse(Call<BoredResponse> call, Response<BoredResponse> response) {
                    if (response.isSuccessful() && response.body() != null){
                        BoredResponse boredResponse = response.body();
                        binding.ifboredText.setText(boredResponse.activity);
                    }
                }

                @Override
                public void onFailure(Call<BoredResponse> call, Throwable t) {
                    Log.e("DEBUG", "Error: " + t.getMessage(), t);
                }
            });
        });

        return binding.getRoot();
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                android.Manifest.permission.INTERNET,
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            is_permissions_granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

    }
}