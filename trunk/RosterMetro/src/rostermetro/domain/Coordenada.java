package rostermetro.domain;

import java.awt.geom.Point2D;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Coordenada extends Point2D.Double {

    public static final double RADIOTIERRA = 6371; //km

    public Coordenada(double longitud, double latitud) {
        super(longitud, latitud);
    }

    /*
     * In meters
     * ByHaversineFormula
     */
    public double getHarversineDistanceTo(Coordenada coordTo) {

        double lat1 = Math.toRadians(getLatitude());
        double lon1 = Math.toRadians(getLongitude());
        double lat2 = Math.toRadians(coordTo.getLatitude());
        double lon2 = Math.toRadians(coordTo.getLongitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double sinlat = Math.sin(dlat / 2);
        double sinlon = Math.sin(dlon / 2);

        double a = (sinlat * sinlat) + Math.cos(lat1) * Math.cos(lat2) * (sinlon * sinlon);
        double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));

        double distanceInMeters = RADIOTIERRA * c * 1000;

        return distanceInMeters;

    }

    private double getLatitude() {
        return y;
    }

    private double getLongitude() {
        return x;
    }

    @Override
    public String toString() {
        return "Coordenada[" + x + ", " + y + "]";
    }
}
