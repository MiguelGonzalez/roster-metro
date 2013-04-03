package rostermetro.domain;

import java.util.Objects;
import rostermetro.Utilidades;

/**
 *
 * @author paracaidista
 */
public class Parada {

    private final String nombre;
    private final Coordenada coordenada;

    public Parada(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, getNombre());
        Utilidades.appendLine(str, "-", coordenada.toString());
        return str.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getNombre());
        hash = 97 * hash + Objects.hashCode(this.coordenada);
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
        if (!Objects.equals(this.coordenada, other.coordenada)) {
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
}
