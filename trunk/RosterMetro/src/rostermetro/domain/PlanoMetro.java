/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rostermetro.Utilidades;

/**
 *
 * @author Ceura
 */
public class PlanoMetro {

    private final String nombre;
    private final List<Linea> lineas;
    private final HashMap<Parada, List<Linea>> paradaYLineas;

    public PlanoMetro(String nombre, List<Linea> lineas) {
        this.nombre = nombre;
        this.lineas = lineas;
        paradaYLineas = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, nombre);
        for(Linea linea : lineas){
            Utilidades.appendLine(str, linea.toString());
        }
        return str.toString();
    }

    
}
