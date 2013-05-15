package rostermetro.busqueda.conLinea;

import java.util.ArrayList;
import java.util.List;
import rostermetro.domain.Linea;

/**
 * Contenedor de las líneas por las que pasa una ruta, y si la línea es
 * distinta a la anterior suma un transbordo.
 * 
 * @author Jaime Bárez y Miguel González
 */
public class NodosLinea {

    private final List<Linea> lineasRecorridas;
    private int numTransbordos;

    /**
     * Construye el objeto con una línea de inicio.
     * @param miLinea 
     */
    public NodosLinea(Linea miLinea) {
        lineasRecorridas = new ArrayList<>();
        lineasRecorridas.add(miLinea);
        numTransbordos = 0;
    }

    /**
     * Construye el objeto a partir de otro objeto NodosLinea. Otra manera
     * de hacerse hubiera sido implementar el método clone() para obtener
     * una copia del mismo objeto.
     * 
     * @param nodosLinea 
     */
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
