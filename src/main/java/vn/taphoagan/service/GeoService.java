package vn.taphoagan.service;

import org.springframework.stereotype.Service;

@Service
public class GeoService {

    public double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLng / 2), 2);
        return r * 2 * Math.asin(Math.sqrt(a));
    }

    public String formatDistance(double km) {
        return Math.max(0, Math.round(km * 10) / 10.0) + " km";
    }
}
