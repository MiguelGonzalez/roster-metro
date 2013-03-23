/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

/**
 *
 * @author paracaidista
 */
public class Coordenada {
    public double longitude;
    public double latitude;

    public Coordenada(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public int calculateDistanceByHaversineFormula(Coordenada coordenadaA) {

        double earthRadius = 6371; // km

        double lat1 = Math.toRadians(latitude);
        double lon1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(coordenadaA.latitude);
        double lon2 = Math.toRadians(coordenadaA.longitude);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double sinlat = Math.sin(dlat / 2);
        double sinlon = Math.sin(dlon / 2);

        double a = (sinlat * sinlat) + Math.cos(lat1) * Math.cos(lat2) * (sinlon * sinlon);
        double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));

        double distanceInMeters = earthRadius * c * 1000;

        return (int) distanceInMeters;

    }
}
