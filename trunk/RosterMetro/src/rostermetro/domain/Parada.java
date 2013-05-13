package rostermetro.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa una parada con nombre y coordenadas
 *
 * @author Jaime Bárez y Miguel González
 */
public class Parada {

    protected final String nombre;
    private final Coordenada coordenada;
    private final Set<Linea> correspondencias;

    public Parada(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
        this.correspondencias = new HashSet<>();
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the coordenada
     */
    public Coordenada getCoordenada() {
        return coordenada;
    }

    /**
     * Devuelve la distancia Haversine entre las dos paradas
     *
     * @param paradaTo
     * @return
     */
    public double getDistancia(Parada paradaTo) {
        return coordenada.getHarversineDistanceTo(paradaTo.getCoordenada());
    }

    /**
     * Devuelve las líneas con las que tiene correspondencia.
     *
     * @return the correspondencias
     */
    public Set<Linea> getCorrespondencias() {
        return Collections.unmodifiableSet(correspondencias);
    }

    /**
     * Devuelve las paradas directamente conectadas
     *
     * @return
     */
    public Set<Parada> getSucesores() {
        Set<Parada> sucesores = new HashSet<>();
        for (Linea correspondencia : getCorrespondencias()) {
            sucesores.addAll(correspondencia.getParadasQueRodean(this));
        }
        return sucesores;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }
    /**
     * Añade una correspondencia
     * @param linea 
     */
    public void addCorrespondencia(Linea linea) {
        correspondencias.add(linea);
    }
    /**
     * Devuelve las líneas comunes con la parada dada
     * @param p2
     * @return 
     */
    public Set<Linea> getLineasComunes(Parada p2) {
        Set<Linea> lineasComunes = new HashSet<>();
        if (p2 != null) {
            lineasComunes.addAll(getCorrespondencias());
            //Interseccion
            lineasComunes.retainAll(p2.getCorrespondencias());
        }
        return lineasComunes;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Parada other = (Parada) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
