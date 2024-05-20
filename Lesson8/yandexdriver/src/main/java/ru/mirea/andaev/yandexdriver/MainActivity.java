package ru.mirea.andaev.yandexdriver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.andaev.yandexdriver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener, DrivingSession.DrivingRouteListener {
    private ActivityMainBinding binding;
    private boolean is_permissions_granted = false;
    static final private int REQUEST_CODE_PERMISSION = 200;
    private final Point STROMYNKA_LOCATION = new Point(55.794246, 37.701415);
    private UserLocationLayer userLocationLayer;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private MapKit mapKit;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private MapObjectTapListener listener = null;
    private PlacemarkMapObject marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        MakePermissionsRequest();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapView = binding.mapview;
        mapView.getMap().setRotateGesturesEnabled(true);
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        listener = new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                Toast.makeText(getApplication(),"STROMYNKA", Toast.LENGTH_SHORT).show();
                return false;
            }

        };
        marker = mapObjects.addPlacemark(STROMYNKA_LOCATION);
        marker.addTapListener(listener);
        marker.setVisible(true);
        marker.setText("MIREA_STROMYNKA_20");
        mapKit = MapKitFactory.getInstance();
        mapKit.createLocationManager().requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                submitRequest(location.getPosition(), STROMYNKA_LOCATION);
                mapView.getMap().move(new CameraPosition(location.getPosition(), 11.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
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
        final boolean internet_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
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
    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }
    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "Err";
        if (error instanceof RemoteError) {
            errorMessage = "Instance err";
        } else if (error instanceof NetworkError) {
            errorMessage = "netwowrk err";
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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

    private void submitRequest(Point ROUTE_START_LOCATION, Point ROUTE_END_LOCATION) {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(4);
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();

        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));

        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions,
                vehicleOptions,this);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}