/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda.conLinea;

import java.util.List;
import rostermetro.busqueda.Ruta;

/**
 *
 * @author paracaidista
 */
public class RutaConLinea extends Ruta<ParadaRutaConLinea> {
    public RutaConLinea(List<ParadaRutaConLinea> paradasRuta) {
        super(paradasRuta);
    }
}