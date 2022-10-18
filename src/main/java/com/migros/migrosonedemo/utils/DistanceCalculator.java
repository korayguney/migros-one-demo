package com.migros.migrosonedemo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DistanceCalculator {
    public static double calculateDistanceInMeters(double lat1, double long1, double lat2, double long2) {
        double dist = org.apache.lucene.util.SloppyMath.haversinMeters(lat1, long1, lat2, long2);
        return dist;
    }
}
