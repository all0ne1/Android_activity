package ru.mirea.andaev.mireaproject.ui.lesson6;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;

import ru.mirea.andaev.mireaproject.databinding.FragmentProfilePhotoBinding;

public class profilePhotoFragment extends Fragment implements ActivityResultCallback<ActivityResult> {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final String PREFS_NAME = "ProfilePhotoPrefs";
    private static final String PREFS_KEY_IMAGE_URI = "imageUri";

    private Uri imageUri;
    private FragmentProfilePhotoBinding binding;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public profilePhotoFragment() {

    }

    public static profilePhotoFragment newInstance(String param1, String param2) {
        profilePhotoFragment fragment = new profilePhotoFragment();
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

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilePhotoBinding.inflate(inflater, container, false);

        loadSavedImageUri();

        binding.updateProfilePhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

        return binding.getRoot();
    }

    private void openCamera() {
        try {
            imageUri = getImageUri();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraActivityResultLauncher.launch(cameraIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri() throws IOException {
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile("user_photo", ".jpg", storageDirectory);
        String authorities = getContext().getPackageName() + ".fileprovider";
        return FileProvider.getUriForFile(getActivity(), authorities, file);
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            binding.profilePhotoView.setImageURI(imageUri);
            saveImageUri(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    private void saveImageUri(Uri uri) {
        getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(PREFS_KEY_IMAGE_URI, uri.toString())
                .apply();
    }

    private void loadSavedImageUri() {
        String uriString = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(PREFS_KEY_IMAGE_URI, null);
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            binding.profilePhotoView.setImageURI(imageUri);
        }
    }
}
