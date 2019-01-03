package com.breathsafe.kth.breathsafe;

import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;

import java.util.ArrayList;
import java.util.List;

public class CalculateAirPollutionData {
    private static final String TAG = "CalculateAirPollutionDa";
    private static final double AVERAGE_RADIUS_OF_EARTH_M = 6378137;
    private static final int nrOfSensorsToUse = 5;
    private static final double P1_TO_AQI = 1.5;
    private static final double P2_to_AQI = 0.35;

    public static double weightedPM1andPM2(List<AirPollution> originalList, Location location) {
        long start = System.currentTimeMillis();
        ArrayList<AirPollution> list = (ArrayList<AirPollution>)((ArrayList<AirPollution>)originalList).clone();
        if (list.size() <= 0)
            return -1;
        List<Integer> distances = new ArrayList<>();
        List<Integer> distancesToUse = new ArrayList<>();
        fillDistances(list, distances, location);
        List<AirPollution> toCalculate = new ArrayList<>();
        while (toCalculate.size() < nrOfSensorsToUse && list.size() > 0) {
            toCalculate.add(findClosest(list, distances, distancesToUse));
        }

        double average = caculateAverage(toCalculate, distancesToUse);
        Log.i(TAG, "weightedPM1andPM2: Time to calculate: " + (System.currentTimeMillis() - start));
        Log.i(TAG, "weightedPM1andPM2: AQI: " + average);
        return average;
    }

    private static double caculateAverage(List<AirPollution> list, List<Integer> distances) {
        int totalDistance = 0, totalDistance2 = 0;
        double weightedP1 = 0.0, weightedP2 = 0.0;
        for (Integer i : distances)
            totalDistance += i;
        List<Integer> distances2 = new ArrayList<>();
        for (Integer i : distances) {
            int dist = (totalDistance - i);
            distances2.add(dist);
            totalDistance2 += dist;
        }
        for (int i = 0; i < list.size(); i++) {
            int dist = distances2.get(i);
            AirPollution ap = list.get(i);
            double weight = (double)((double)dist / (double)totalDistance2);
            weightedP1 += (weight * ap.getP1());
            weightedP2 += (weight * ap.getP2());
//            Log.i(TAG, "caculateAverage: weightedP1: " + (weight * ap.getP1()));
//            Log.i(TAG, "caculateAverage: weightedP2: " + (weight * ap.getP1()));
//            Log.i(TAG, "caculateAverage: Dist: " + distances.get(i));
//            Log.i(TAG, "caculateAverage: P1: " + ap.getP1());
//            Log.i(TAG, "caculateAverage: P2: " + ap.getP2());
        }

//        Log.i(TAG, "caculateAverage: TotalDist: " + totalDistance2);
//        Log.i(TAG, "caculateAverage: Total P1: " + weightedP1);
//        Log.i(TAG, "caculateAverage: Total P2: " + weightedP2);

        double AQIP1 = (weightedP1 / P1_TO_AQI);
        double AQIP2 = (weightedP2 / P2_to_AQI);
//        Log.i(TAG, "caculateAverage: AQIP1: " + AQIP1);
//        Log.i(TAG, "caculateAverage: AQIP2: " + AQIP2);
        double averageAQI = (AQIP1 + AQIP2);
//        Log.i(TAG, "caculateAverage: AQI average: " + averageAQI);

        return averageAQI;
    }

    private static AirPollution findClosest(List<AirPollution> list, List<Integer> distances, List<Integer> distancesToUse) {
        if (list.size() == 0)
            return null;
        if (list.size() == 1) {
            distancesToUse.add(distances.remove(0));
            return list.remove(0);
        }

        int pos = 0;
        Integer closest = distances.get(0);
        for (int i = 1; i < distances.size(); i++) {
            Integer next = distances.get(i);
            if (next < closest) {
                closest = next;
                pos = i;
            }
        }
        distancesToUse.add(distances.remove(pos));
        return list.remove(pos);
    }

    private static void fillDistances(List<AirPollution> list, List<Integer> distances, Location location) {
        for (AirPollution a : list) {
            distances.add(calculateDistanceInMeter(a.getLatitude(), a.getLongitude(), location.getLatitude(), location.getLongitude()));
        }
    }


    private static int calculateDistanceInMeter(double userLat, double userLng, double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_M * c));
    }
}
