package ru.mirea.andaev.yandexmaps;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class YandexMapApp extends Application {
    private final static String MAPKIT_API_KEY = "4de15af4-3785-470f-ac34-2a6f597d0f14";
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
