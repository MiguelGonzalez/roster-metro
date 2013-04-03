package rostermetro.domain;

import rostermetro.Utilidades;

/**
 *
 * @author paracaidista
 */
public class Coordenada {

    public static final double EARTHRADIUS = 6371; //km
    private final double longitude;
    private final double latitude;

    public Coordenada(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int calculateDistanceByHaversineFormula(Coordenada coordenadaA) {

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

        double distanceInMeters = EARTHRADIUS * c * 1000;

        return (int) distanceInMeters;

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.valueOf(longitude)).append("").append(String.valueOf(latitude));
        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordenada other = (Coordenada) obj;
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        return hash;
    }
}
