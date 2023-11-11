package com.havelsan.backend.service.Imp;

import com.havelsan.backend.service.MapService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MapServiceImpl implements MapService {

    private static final int LENGTH = 100;
    private static final double UPDATE_MIN = -0.8;
    private static final double UPDATE_MAX = 1.90;
    private static final double TURKEY_MIN_LAT = 36.0;
    private static final double TURKEY_MAX_LAT = 42.0;
    private static final double TURKEY_MIN_LNG = 26.0;
    private static final double TURKEY_MAX_LNG = 45.0;

    private static List<List<Double>> markerPositions;

    @Override
    public Map<String, Object> randomMarkerPosition() {
        Random random = new Random();

        if (markerPositions == null || markerPositions.isEmpty()) {
            markerPositions = new ArrayList<>();
            for (int i = 0; i < LENGTH; i++) {
                markerPositions.add(generateRandomCoordinate());
            }
        } else {
            for (List<Double> position : markerPositions) {
                Double updatedLat = position.get(0) + (random.nextDouble() > 0.5 ? 1 : -1) * (UPDATE_MIN + (UPDATE_MAX - UPDATE_MIN) * random.nextDouble());
                Double updatedLng = position.get(1) + (random.nextDouble() > 0.5 ? 1 : -1) * (UPDATE_MIN + (UPDATE_MAX - UPDATE_MIN) * random.nextDouble());
                position.set(0, updatedLat);
                position.set(1, updatedLng);
            }
        }

        double maxLat = markerPositions.stream().mapToDouble(pos -> pos.get(0)).max().orElse(TURKEY_MAX_LAT);
        double minLat = markerPositions.stream().mapToDouble(pos -> pos.get(0)).min().orElse(TURKEY_MIN_LAT);
        double maxLng = markerPositions.stream().mapToDouble(pos -> pos.get(1)).max().orElse(TURKEY_MAX_LNG);
        double minLng = markerPositions.stream().mapToDouble(pos -> pos.get(1)).min().orElse(TURKEY_MIN_LNG);

        double centerLat = (maxLat + minLat) / 2;
        double centerLng = (maxLng + minLng) / 2;

        int zoom = calculateZoom(maxLat, minLat, maxLng, minLng);

        return Map.of("position", markerPositions, "center", List.of(centerLat, centerLng), "zoom", zoom);
    }

    private int calculateZoom(double maxLat, double minLat, double maxLng, double minLng) {
        double mapWidth = 700;
        double mapHeight = 550;

        double mapSpanX = maxLng - minLng;
        double mapSpanY = maxLat - minLat;

        double zoomX = Math.log(mapWidth / mapSpanX) / Math.log(2);
        double zoomY = Math.log(mapHeight / mapSpanY) / Math.log(2);

        double zoom = (zoomX + zoomY) / 2;

        return (int) Math.round(zoom);
    }

    private List<Double> generateRandomCoordinate() {
        Random random = new Random();
        Double lat = TURKEY_MIN_LAT + (TURKEY_MAX_LAT - TURKEY_MIN_LAT) * random.nextDouble();
        Double lng = TURKEY_MIN_LNG + (TURKEY_MAX_LNG - TURKEY_MIN_LNG) * random.nextDouble();

        List<Double> position = new ArrayList<>();
        position.add(lat);
        position.add(lng);

        return position;
    }
}
