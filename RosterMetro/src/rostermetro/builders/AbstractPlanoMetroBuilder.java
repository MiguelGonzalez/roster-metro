package rostermetro.builders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class AbstractPlanoMetroBuilder<P, L> {

    private final Map<L, Linea> genericLYSuLinea;
    private final HashSet<Parada> paradas;

    public AbstractPlanoMetroBuilder() {
        genericLYSuLinea = new LinkedHashMap<>();
        paradas = new HashSet<>();
    }

    public void put(P paradaGeneric, L lineaGeneric) {
        
        Linea linea = genericLYSuLinea.get(lineaGeneric);
        if (linea == null) {
            linea = createLinea(lineaGeneric);
            genericLYSuLinea.put(lineaGeneric, linea);
        }
        
        Parada posibleParadaToAdd = createParada(paradaGeneric);
        Parada paradaToAdd = null;
        if (paradas.contains(posibleParadaToAdd)) {//Gracias a sobreescribir el equals
            Iterator<Parada> iterator = paradas.iterator();
            while (iterator.hasNext()) {
                Parada next = iterator.next();
                if (next.equals(posibleParadaToAdd)) {
                    paradaToAdd = next;
                    break;
                }
            }
        } else {
            paradas.add(posibleParadaToAdd);
            paradaToAdd = posibleParadaToAdd;
        }
        paradaToAdd.getCorrespondencias().add(linea);
        linea.getParadas().add(paradaToAdd);
    }

    public PlanoMetro createPlanoMetro(String name) {
        List<Linea> lineas = new ArrayList<>();
        Iterator<Entry<L, Linea>> iterator = genericLYSuLinea.entrySet().iterator();
        while (iterator.hasNext()) {
            Linea lineaToAdd = iterator.next().getValue();
            lineas.add(lineaToAdd);
        }
        return new PlanoMetro(name, lineas, paradas);
    }

    public void flush() {
        genericLYSuLinea.clear();
        paradas.clear();
    }

    protected abstract Linea createLinea(L lineaGeneric);

    protected abstract Parada createParada(P paradaGeneric);
}
