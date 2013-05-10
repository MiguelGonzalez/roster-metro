package rostermetro.busqueda.conLinea;

import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 * Parada calculada por una ruta con la línea por la que se pasa.
 *
 * @author Jaime Bárez y Miguel González
 */
public class ParadaRutaConLinea extends Parada {

    private Linea estaLinea;

    public ParadaRutaConLinea(Parada parada, Linea estaLinea) {
        super(parada.getNombre(), parada.getCoordenada());

        this.estaLinea = estaLinea;
    }

    public ParadaRutaConLinea(String nombre,
            Coordenada coordenada) {
        super(nombre, coordenada);
    }

    public Linea getLinea() {
        return estaLinea;
    }

    @Override
    public String toString() {
        return nombre + ", linea: " + estaLinea;
    }
}
