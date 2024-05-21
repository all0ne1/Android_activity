package ru.mirea.andaev.mireaproject.ui.lesson8.api;

import java.util.List;

public class OSRMResponse {
    public List<Route> routes;

    public class Route {
        public Geometry geometry;
    }

    public class Geometry {
        public List<List<Double>> coordinates;
    }
}