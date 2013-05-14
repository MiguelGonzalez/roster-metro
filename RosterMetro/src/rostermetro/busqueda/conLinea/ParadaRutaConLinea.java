package rostermetro.busqueda.conLinea;

import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 * Parada calculada por una BusquedaRutaConLinea. Contiene información de la
 * línea por la que ha pasado
 *
 * @author Jaime Bárez y Miguel González
 */
public class ParadaRutaConLinea extends Parada {

    private final Linea estaLinea;

    public ParadaRutaConLinea(Parada parada, Linea estaLinea) {
        super(parada.getNombre(), parada.getCoordenada());
        this.estaLinea = estaLinea;
    }

    public Linea getLinea() {
        return estaLinea;
    }

    @Override
    public String toString() {
        return nombre + ", linea: " + estaLinea;
    }
}
