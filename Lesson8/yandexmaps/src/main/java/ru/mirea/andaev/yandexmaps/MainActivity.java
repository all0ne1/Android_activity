package ru.mirea.andaev.yandexmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.mirea.andaev.yandexmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    static final private int REQUEST_CODE_PERMISSION = 200;
    private boolean is_permissions_granted = false;
    private UserLocationLayer userLocationLayer;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        MakePermissionsRequest();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mapview.getMap().move(
            new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapview.onStart();
        MapKitFactory.getInstance().onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        binding.mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }

    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            is_permissions_granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (is_permissions_granted) {
                loadUserLocationLayer();
            } else {
                finish();
            }
        }
    }
    private void MakePermissionsRequest() {
        final boolean internet_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        final boolean coarse_location_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        final boolean fine_location_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        final boolean background_location_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        is_permissions_granted = coarse_location_enabled && fine_location_enabled && background_location_enabled && internet_enabled;
        if (is_permissions_granted) {
            loadUserLocationLayer();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.INTERNET
                    }, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(binding.mapview.getWidth() * 0.5),
                        (float)(binding.mapview.getHeight() * 0.5)),
                new PointF((float)(binding.mapview.getWidth() * 0.5),
                        (float)(binding.mapview.getHeight() * 0.83)));
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, android.R.drawable.arrow_up_float));
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.ic_launcher_foreground),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)

                        .setZIndex(1f)
                        .setScale(0.5f)

        );
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}