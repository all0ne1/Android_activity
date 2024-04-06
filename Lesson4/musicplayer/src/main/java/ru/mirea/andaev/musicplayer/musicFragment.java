package ru.mirea.andaev.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mirea.andaev.musicplayer.databinding.FragmentMusicBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link musicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class musicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public musicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment music.
     */
    // TODO: Rename and change types and number of parameters
    public static musicFragment newInstance(String param1, String param2) {
        musicFragment fragment = new musicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    MediaPlayer mPlayer;
    FragmentMusicBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMusicBinding.inflate(getLayoutInflater());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void stopPlay(){
        mPlayer.stop();
        binding.pauseButton.setEnabled(false);
        binding.stopButton.setEnabled(false);
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
            binding.playButton.setEnabled(true);
        }
        catch (Throwable t) {
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPlayer = MediaPlayer.create(getContext(),R.raw.music);
        binding = FragmentMusicBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
                binding.playButton.setEnabled(false);
                binding.pauseButton.setEnabled(true);
                binding.stopButton.setEnabled(true);
            }
        });
        binding.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                binding.playButton.setEnabled(true);
                binding.pauseButton.setEnabled(false);
                binding.stopButton.setEnabled(true);
            }
        });
        binding.stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stopPlay();
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}