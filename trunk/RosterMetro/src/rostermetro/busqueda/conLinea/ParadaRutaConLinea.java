/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda.conLinea;

import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * @author paracaidista
 */
public class ParadaRutaConLinea extends Parada {
    
    private Linea estaLinea;
    
    public ParadaRutaConLinea(String nombre, 
            Coordenada coordenada, Linea estaLinea) {
        super(nombre, coordenada);
        
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
