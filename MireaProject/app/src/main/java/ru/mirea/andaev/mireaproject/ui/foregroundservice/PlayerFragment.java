package ru.mirea.andaev.mireaproject.ui.foregroundservice;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.mirea.andaev.mireaproject.MainActivity;
import ru.mirea.andaev.mireaproject.R;
import ru.mirea.andaev.mireaproject.databinding.ActivityMainBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {
    static private final int PermissionCode = 200;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        boolean foreground_service_permission = ContextCompat.checkSelfPermission(getActivity(),FOREGROUND_SERVICE)
                == PackageManager.PERMISSION_GRANTED;
        boolean notification_permission = ContextCompat.checkSelfPermission(getActivity(),POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
        String message = (notification_permission && foreground_service_permission)
                ? "Привилегии получены" : "В привилегиях отказано";
        Log.d(MainActivity.class.getSimpleName(), message);
        if (!(foreground_service_permission && notification_permission)){
            ActivityCompat.requestPermissions(getActivity(), new String[]{FOREGROUND_SERVICE,POST_NOTIFICATIONS},PermissionCode);
        }
        Button playButton = v.findViewById(R.id.playButton);
        Button pauseButton = v.findViewById(R.id.pauseButton);
        Button stopButton = v.findViewById(R.id.stopPlay);
        playButton.setOnClickListener(view ->{
            Log.d("PlayerService", "PlayButton");
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.setAction("PLAY");
            startForegroundService(getContext(),serviceIntent);
        });
        stopButton.setOnClickListener(view ->{
            getActivity().stopService(new Intent(getContext(),PlayerService.class));
        });
        pauseButton.setOnClickListener(view ->{
            Intent pauseIntent = new Intent(getContext(), PlayerService.class);
            pauseIntent.setAction("PAUSE");
            getContext().startService(pauseIntent);
        });
        return v;
    }
}