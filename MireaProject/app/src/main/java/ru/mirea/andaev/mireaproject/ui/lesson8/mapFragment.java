package ru.mirea.andaev.mireaproject.ui.lesson8;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.andaev.mireaproject.databinding.FragmentMapBinding;
import ru.mirea.andaev.mireaproject.ui.lesson8.api.OSRMResponse;
import ru.mirea.andaev.mireaproject.ui.lesson8.api.OSRMService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private boolean is_permissions_granted = false;
    private MapView mapView;
    private FragmentMapBinding binding;
    private MyLocationNewOverlay myLocationOverlay;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mapFragment newInstance(String param1, String param2) {
        mapFragment fragment = new mapFragment();
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
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        checkAndRequestPermissions();

    }




    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        binding.mapView.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);


        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(new GeoPoint(55.794229, 37.700772));

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);


        addMarkers();
        List<GeoPoint> points = getMarkerPoints();

        drawRoute(points.get(0),points.get(1));
        return binding.getRoot();
    }

    private void drawRoute(GeoPoint start, GeoPoint end) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://router.project-osrm.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OSRMService service = retrofit.create(OSRMService.class);
        Call<OSRMResponse> call = service.getRoute(start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude());
        call.enqueue(new retrofit2.Callback<OSRMResponse>() {
            @Override
            public void onResponse(Call<OSRMResponse> call, retrofit2.Response<OSRMResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GeoPoint> routePoints = new ArrayList<>();
                    List<List<Double>> coordinates = response.body().routes.get(0).geometry.coordinates;
                    for (List<Double> coord : coordinates) {
                        routePoints.add(new GeoPoint(coord.get(1), coord.get(0)));
                    }
                    Polyline line = new Polyline();
                    line.setPoints(routePoints);
                    mapView.getOverlays().add(line);
                    mapView.invalidate();
                }
            }
            @Override
            public void onFailure(Call<OSRMResponse> call, Throwable t) {
                Log.d("DEBUG","Smthng go wrond");
                Log.e("DEBUG", "Error: " + t.getMessage(), t);
            }
        });
    }



    private void addMarkers(){
        Marker metroStation = new Marker(mapView);
        metroStation.setPosition(new GeoPoint(55.738984, 37.548203));
        metroStation.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getContext(),"Metro station Studencheskaya\n Kievsyaka street", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(metroStation);
        metroStation.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_center_map, null));

        Marker university = new Marker(mapView);
        university.setPosition(new GeoPoint(55.794449, 37.701319));
        university.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getContext(),"RTU MIREA \n Stromynka 20", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mapView.getOverlays().add(university);
        university.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_center_map, null));

    }

    private List<GeoPoint> getMarkerPoints() {
        List<GeoPoint> markerPoints = new ArrayList<>();
        for (int i = 0; i < mapView.getOverlays().size(); i++) {
            if (mapView.getOverlays().get(i) instanceof Marker) {
                Marker marker = (Marker) mapView.getOverlays().get(i);
                markerPoints.add(marker.getPosition());
            }
        }
        return markerPoints;
    }


    private void checkAndRequestPermissions() {
        String[] permissions = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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