package ru.mirea.andaev.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.File;
import java.io.IOException;

import ru.mirea.andaev.audiorecord.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private	ActivityMainBinding	binding;
    private	static final int REQUEST_CODE_PERMISSION = 200;
    private	final String TAG = MainActivity.class.getSimpleName();
    private	String	fileName = null;
    private	Button recordButton	= null;
    private Button playButton = null;
    private	MediaRecorder recorder	= null;
    private	MediaPlayer	player = null;
    boolean	isStartRecording =	true;
    boolean isStartPlaying = true;
    boolean permissions_granted;
    String recordFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        boolean audioRecordPermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        boolean storagePermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions_granted = (audioRecordPermission && storagePermission) ? permissions_granted = true : false;
        if (!permissions_granted){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        binding.playButton.setEnabled(false);
        recordButton = binding.recordButton;
        playButton = binding.playButton;

        recordFilePath	=	(new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
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
            recordButton.setEnabled(true);
            playButton.setEnabled(true);
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
        } else {
            finish();
        }
    }

}