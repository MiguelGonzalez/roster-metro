package rostermetro.busqueda;

import java.util.Arrays;
import java.util.List;
import rostermetro.domain.Parada;

/**
 * Lista de paradas
 *
 * @author Jaime Bárez y Miguel González
 * @param <P> Clase Genérica de una parada
 */
public class Ruta<P extends Parada> {

    private final List<P> paradas;

    public Ruta(List<P> paradas) {
        this.paradas = paradas;
    }

    public List<P> getListadoParadas() {
        return paradas;
    }

    @Override
    public String toString() {
        return "Ruta: \n" + Arrays.toString(paradas.toArray());
    }
}
