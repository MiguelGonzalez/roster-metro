/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

import java.util.List;

/**
 *
 * @author Ceura
 */
public class PlanoMetro {
    public String nombre;
    public List<Linea> lineas;

    public PlanoMetro(String nombre, List<Linea> lineas) {
        this.nombre = nombre;
        this.lineas = lineas;
    }
}
