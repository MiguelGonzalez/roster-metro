/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

import java.util.List;

/**
 *
 * @author paracaidista
 */
public class Linea {
    public String nombre;
    
    public List<Parada> paradas;

    public Linea(String nombre, List<Parada> paradas) {
        this.nombre = nombre;
        this.paradas = paradas;
    }
}
