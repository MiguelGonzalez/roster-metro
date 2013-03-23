/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

/**
 *
 * @author paracaidista
 */
public class Parada {
    public String nombre;
    public Coordenada coordenada;

    public Parada(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    }
    
    
}
