package rostermetro.busqueda;

import java.util.Arrays;
import java.util.List;
import rostermetro.domain.Parada;

public class Ruta {

    private List<Parada> paradas;

    public Ruta(List<Parada> paradas) {
        this.paradas = paradas;
    }

    public List<Parada> getListadoParadas() {
        return paradas;
    }

    @Override
    public String toString() {
        return "Ruta: \n" + Arrays.toString(paradas.toArray());
    }
}
