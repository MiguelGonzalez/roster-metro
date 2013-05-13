package rostermetro.busqueda.conLinea;

import java.util.List;
import rostermetro.busqueda.Ruta;

/**
 * Ruta compuesta por ParadaRutaConLinea
 * @author Jaime Bárez y Miguel González
 */
public class RutaConLinea extends Ruta<ParadaRutaConLinea> {
    public RutaConLinea(List<ParadaRutaConLinea> paradasRuta) {
        super(paradasRuta);
    }
}