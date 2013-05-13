package rostermetro.busqueda.conLinea;

import java.util.ArrayList;
import java.util.List;
import rostermetro.domain.Linea;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class NodosLinea {
    private List<Linea> lineasRecorridas;
    private int numTransbordos;

    public NodosLinea(Linea miLinea) {
        lineasRecorridas = new ArrayList<>();
        lineasRecorridas.add(miLinea);
        numTransbordos = 0;
    }

    public NodosLinea(NodosLinea nodosLinea) {
        lineasRecorridas = new ArrayList<>(nodosLinea.getLineasTotales());
        numTransbordos = nodosLinea.getTransbordos();
    }

    public Linea getUltimaLinea() {
        return lineasRecorridas.get(lineasRecorridas.size() - 1);
    }

    public void addLinea(Linea linea) {
        if (!getUltimaLinea().equals(linea)) {
            numTransbordos++;
        }
        lineasRecorridas.add(linea);
    }

    public int getTransbordos() {
        return numTransbordos;
    }

    public List<Linea> getLineasTotales() {
        return lineasRecorridas;
    }
}
