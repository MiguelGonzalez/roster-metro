package rostermetro.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import rostermetro.Utilidades;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Linea {

    private final String nombre;
    private final List<Parada> paradas;

    public Linea(String nombre) {
        this.nombre = nombre;
        this.paradas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    private boolean tieneAnterior(Parada parada) {
        return paradas.indexOf(parada) > 0;
    }

    private Parada getAnteriorParada(Parada parada) {
        return paradas.get(paradas.indexOf(parada) - 1);
    }

    private boolean tieneSiguiente(Parada parada) {
        return paradas.indexOf(parada) != paradas.size() - 1;
    }

    private Parada getSiguienteParada(Parada parada) {
        return paradas.get(paradas.indexOf(parada) + 1);

    }

    public List<Parada> getParadasQueRodean(Parada parada) {
        List<Parada> sucesores = new LinkedList<>();
        if (tieneAnterior(parada)) {
            sucesores.add(getAnteriorParada(parada));
        }
        if (tieneSiguiente(parada)) {
            sucesores.add(getSiguienteParada(parada));
        }
        return sucesores;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.nombre);
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
        return true;
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
