package ru.mirea.andaev.mireaproject.ui.lesson5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.mirea.andaev.mireaproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link compasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class compasFragment extends Fragment implements SensorEventListener {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private ImageView compass;
    private SensorManager sensorManager;
    private Sensor magnetic_field_sensor;
    private Sensor accelerometer;
    private float[] lastMagnetometer = new float[3];
    private float[] lastAccelerometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private String mParam1;
    private String mParam2;

    public compasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        magnetic_field_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,magnetic_field_sensor);
        sensorManager.unregisterListener(this,accelerometer);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnetic_field_sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment compasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static compasFragment newInstance(String param1, String param2) {
        compasFragment fragment = new compasFragment();
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
        View v = inflater.inflate(R.layout.fragment_compas, container, false);
        compass = v.findViewById(R.id.compassImage);
        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == magnetic_field_sensor) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        } else if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet){
            SensorManager.getRotationMatrix(rotationMatrix,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix,orientation);
            float rad_azimuth = orientation[0];
            float degree_azimuth = (float) (Math.toDegrees(rad_azimuth) + 360) % 360;

            compass.setRotation(-degree_azimuth);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}