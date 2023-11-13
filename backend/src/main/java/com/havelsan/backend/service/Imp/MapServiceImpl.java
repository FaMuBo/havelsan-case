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
    private static final double UPDATE_MIN = -0.0001;
    private static final double UPDATE_MAX = 0.0010;
    private static final double TURKEY_MIN_LAT = 36.0;
    private static final double TURKEY_MAX_LAT = 42.0;
    private static final double TURKEY_MIN_LNG = 26.0;
    private static final double TURKEY_MAX_LNG = 45.0;

    private static List<List<Double>> markerPositions;

    @Override
    public Map<String, Object> randomMarkerPosition(Integer zoom) {
        System.out.println("yeni zoom: " + zoom);

        if (markerPositions == null || markerPositions.isEmpty()) {
            markerPositions = new ArrayList<>();
            for (int i = 0; i < LENGTH; i++) {
                markerPositions.add(generateRandomCoordinate());
            }
        }

        List<List<Double>> newMarkerPositions = new ArrayList<>();
        Random random = new Random();

        for (List<Double> position : markerPositions) {
            List<Double> newPosition = new ArrayList<>(position);
            newPosition.set(0, newPosition.get(0) + getRandomUpdate(random));
            newPosition.set(1, newPosition.get(1) + getRandomUpdate(random));
            newMarkerPositions.add(newPosition);
        }

        double updateThreshold = getUpdateThreshold(zoom);

        for (int i = 0; i < LENGTH; i++) {
            List<Double> oldPosition = markerPositions.get(i);
            List<Double> newPosition = newMarkerPositions.get(i);

            double latDiff = Math.abs(newPosition.get(0) - oldPosition.get(0));
            double lngDiff = Math.abs(newPosition.get(1) - oldPosition.get(1));

            if (latDiff <= updateThreshold || lngDiff <= updateThreshold) {
                markerPositions.get(i).set(0, newPosition.get(0));
                markerPositions.get(i).set(1, newPosition.get(1));
            }
        }

        return Map.of("position", newMarkerPositions,"zoom", zoom);
    }

    private double getUpdateThreshold(Integer zoom) {
        if (zoom >= 1 && zoom <= 5) {
            return 0.008;
        } else if (zoom >= 6 && zoom <= 11) {
            return 0.006;
        } else if (zoom >= 12 && zoom <= 17) {
            return 0.003;
        } else if (zoom == 18) {
            return 0.001;
        } else {
            return 0.0;
        }
    }

    private double getRandomUpdate(Random random) {
        return (random.nextDouble() > 0.5 ? 1 : -1) * (UPDATE_MIN + (UPDATE_MAX - UPDATE_MIN) * random.nextDouble());
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
