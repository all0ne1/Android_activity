package ru.mirea.andaev.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.andaev.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private Uri imageUri;
    boolean permissions_granted = false;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permissions_granted = PermissionsRequest();
        binding.updateImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(!permissions_granted){
                Log.i(this.getLocalClassName(),"No permissions");
                PermissionsRequest();
            }
            try {
                File photoFile = createImageFile();
                String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                imageUri = FileProvider.getUriForFile(MainActivity.this,	authorities, photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,	imageUri);
                cameraActivityResultLauncher.launch(cameraIntent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>()
    {
        @Override
        public	void onActivityResult(ActivityResult result)	{
            if	(result.getResultCode()	== Activity.RESULT_OK)	{
                Intent data	= result.getData();
                binding.imageView.setImageURI(imageUri);
            }
        }
    };
    ActivityResultLauncher<Intent> cameraActivityResultLauncher	= registerForActivityResult(
            new	ActivityResultContracts.StartActivityForResult(),
            callback);



    private boolean PermissionsRequest(){
        boolean storage_perm = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        boolean camera_perm = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
        );
        if (storage_perm && camera_perm){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {
        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUEST_CODE_PERMISSION);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int	requestCode, @NonNull String[] permissions,	@NonNull int[] grantResults)	{
        super.onRequestPermissionsResult(requestCode,	permissions,	grantResults);
        if	(requestCode	==	REQUEST_CODE_PERMISSION)	{
            permissions_granted	=	grantResults.length	>	0
                    &&	grantResults[0]	==	PackageManager.PERMISSION_GRANTED;
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}