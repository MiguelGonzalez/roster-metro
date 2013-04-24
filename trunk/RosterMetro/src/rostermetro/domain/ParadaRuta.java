/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

/**
 *
 * @author paracaidista
 */
public class ParadaRuta extends Parada {
    
    private String lineaSiguiente;
    private boolean hagoTransbordo;
    
    public ParadaRuta(String nombre, 
            Coordenada coordenada, String lineaSiguiente,
            boolean hagoTransbordo) {
        super(nombre, coordenada);
        
        this.lineaSiguiente = lineaSiguiente;
        this.hagoTransbordo = hagoTransbordo;
    }
    
    public ParadaRuta(String nombre, 
            Coordenada coordenada) {
        super(nombre, coordenada);
    }
    
    public String getLineaSiguiente() {
        return lineaSiguiente;
    }
    
    public boolean isTransbordo() {
        return hagoTransbordo;
    }
    
    @Override
    public String toString() {
        return nombre + ", linea siguiente: " + lineaSiguiente;
    }
    
}
