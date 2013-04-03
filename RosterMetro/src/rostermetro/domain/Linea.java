package rostermetro.domain;

import java.util.List;
import java.util.Objects;
import rostermetro.Utilidades;

/**
 *
 * @author paracaidista
 */
public class Linea {

    private final String nombre;
    private final List<Parada> paradas;

    public Linea(String nombre, List<Parada> paradas) {
        this.nombre = nombre;
        this.paradas = paradas;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, nombre);
        for (Parada parada : paradas) {
            Utilidades.appendLine(str, "-", parada.toString());
        }
        return str.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.nombre);
        hash = 59 * hash + Objects.hashCode(this.paradas);
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
        final Linea other = (Linea) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.paradas, other.paradas)) {
            return false;
        }
        return true;
    }
}
