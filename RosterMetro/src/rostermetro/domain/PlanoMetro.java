/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        for (Linea linea : lineas) {
            for (Parada parada : linea.getParadas()) {
                if (paradaYLineas.containsKey(parada)) {
                    paradaYLineas.get(parada).add(linea);
                } else {
                    ArrayList<Linea> lineasList = new ArrayList<>();
                    lineasList.add(linea);
                    paradaYLineas.put(parada, lineasList);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, nombre);
        for (Linea linea : lineas) {
            Utilidades.appendLine(str, linea.toString());
        }
        return str.toString();
    }

    public Map<Parada, List<Linea>> getParadaYLineas() {
        return paradaYLineas;
    }
}
