package rostermetro.busqueda.conLinea;

import java.util.List;
import rostermetro.busqueda.Ruta;

/**
 * Ruta compuesta por ParadaRutaConLinea. Lo único que hace es restringir el
 * genérico de la función de la que hereda, para usar como unidad la
 * ParadaRutaConLinea.
 *
 * @author Jaime Bárez y Miguel González
 */
public class RutaConLinea extends Ruta<ParadaRutaConLinea> {

    public RutaConLinea(List<ParadaRutaConLinea> paradasRuta) {
        super(paradasRuta);
    }
}