package rostermetro.busqueda;

import java.util.Arrays;
import java.util.List;
import rostermetro.domain.Parada;

public class Ruta<T extends Parada> {

    private List<T> paradas;

    public Ruta(List<T> paradas) {
        this.paradas = paradas;
    }

    public List<T> getListadoParadas() {
        return paradas;
    }

    @Override
    public String toString() {
        return "Ruta: \n" + Arrays.toString(paradas.toArray());
    }
}
