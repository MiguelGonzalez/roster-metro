package rostermetro.domain;

import java.util.List;
import rostermetro.Utilidades;

/**
 *
 * @author Jaime Bárez y Miguel González
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
}
