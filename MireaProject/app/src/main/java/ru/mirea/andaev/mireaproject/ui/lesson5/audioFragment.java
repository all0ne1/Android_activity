package ru.mirea.andaev.mireaproject.ui.lesson5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import ru.mirea.andaev.mireaproject.databinding.FragmentVoiceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link audioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class audioFragment extends Fragment{
    FragmentVoiceBinding binding;
    private	String	fileName = null;
    private	static final int REQUEST_CODE_PERMISSION = 200;
    private	final String TAG = getTag();
    private MediaRecorder recorder	= null;
    private MediaPlayer player = null;
    boolean	isStartRecording =	true;
    boolean isStartPlaying = true;
    boolean permissions_granted;
    String recordFilePath;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public audioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment voiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static audioFragment newInstance(String param1, String param2) {
        audioFragment fragment = new audioFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVoiceBinding.inflate(inflater,container,false);
        boolean audioRecordPermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO);
        boolean storagePermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // Inflate the layout for this fragment
        permissions_granted = (audioRecordPermission && storagePermission) ? permissions_granted = true : false;
        if (!permissions_granted){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        binding.playRecordedButton.setEnabled(false);
        Button recordButton = binding.recordButton;
        Button playButton = binding.playRecordedButton;

        recordFilePath	=	(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();

        recordButton.setOnClickListener(v ->{
            if	(isStartRecording)	{
                startRecord();
                recordButton.setText("Stop	recording");
                playButton.setEnabled(false);
            }	else	{
                stopRecord();
                recordButton.setText("Start	recording");
                playButton.setEnabled(true);
            }
            isStartRecording = !isStartRecording;
        });

        playButton.setOnClickListener(v->{
            if (isStartPlaying) {
                startPlaying();
                playButton.setText("Stop playing");
                recordButton.setEnabled(false);
                isStartPlaying = false;
            } else {
                stopPlaying();
                playButton.setText("Start playing");
                recordButton.setEnabled(true);
                isStartPlaying = true;
            }
        });
        return binding.getRoot();
    }
    void startRecord(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try	{
            recorder.prepare();
        }	catch	(IOException e)	{
            Log.e(TAG,	"prepare()	failed");
        }
        recorder.start();
        Log.i(TAG,"Recording...");
    }

    void stopRecord() {
        recorder.stop();
        recorder.release();
        recorder = null;
        Log.i(TAG,"Recorded");
    }
    void startPlaying() {
        player = new MediaPlayer();
        Button playButton = binding.playRecordedButton;
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mp ->{
                stopPlaying();
                playButton.setText("Start playing");
                playButton.setEnabled(true);
                isStartPlaying = true;
            });
            Log.i(TAG,"Record is playing");
        } catch (IOException e) {
            Log.e(TAG, "prepare()	failed");
        }
    }

    void stopPlaying()	{
        if (player != null) {
            binding.recordButton.setEnabled(true);
            binding.playRecordedButton.setEnabled(true);
            player.release();
            player = null;
            isStartPlaying = true;
            Log.i(TAG,"Record stopped playing");
        }
    }
    @Override
    public void onRequestPermissionsResult(int	requestCode, @NonNull String[]	permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            permissions_granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }


}