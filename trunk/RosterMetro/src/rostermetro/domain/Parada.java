package rostermetro.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import rostermetro.busqueda.conLinea.BusquedaRutaConLinea;

/**
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

    public double getDistancia(Parada paradaTo) {
        return coordenada.getHarversineDistanceTo(paradaTo.getCoordenada());
    }

    /**
     * @return the correspondencias
     */
    public Set<Linea> getCorrespondencias() {
        return Collections.unmodifiableSet(correspondencias);
    }

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

    public void addCorrespondencia(Linea linea) {
        correspondencias.add(linea);
    }

    public Set<Linea> getLineasComunes(Parada p2) {
        Set<Linea> lineasComunes = new HashSet<>();
        if (p2 != null) {
            lineasComunes.addAll(getCorrespondencias());
            //Interseccion
            lineasComunes.retainAll(p2.getCorrespondencias());
        }
        return lineasComunes;

    }
}
