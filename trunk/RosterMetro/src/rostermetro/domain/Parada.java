package rostermetro.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import rostermetro.Utilidades;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Parada {

    private final String nombre;
    private final Coordenada coordenada;
    private final Collection<Linea> correspondencias;

    public Parada(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
        this.correspondencias = new HashSet<>();;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, getNombre());
        Utilidades.appendLine(str, "-", getCoordenada().toString());
        return str.toString();
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
     * @return the correspondencias
     */
    public Collection<Linea> getCorrespondencias() {
        return correspondencias;
    }
}
