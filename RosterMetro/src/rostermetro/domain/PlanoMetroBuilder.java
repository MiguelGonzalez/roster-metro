package rostermetro.domain;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.Placemark;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author ceura
 */
public class PlanoMetroBuilder {

    private final Map<Folder, Linea> foldersYSuLinea;
    private final HashSet<Parada> paradas;

    public PlanoMetroBuilder() {
        foldersYSuLinea = new LinkedHashMap<>();
        paradas = new HashSet<>();
    }

    public void registrarLinea(Folder lineaKML) {
        Linea lineaARegistrar = new Linea(lineaKML.getName());
        foldersYSuLinea.put(lineaKML, lineaARegistrar);
    }

    //Rellenamos paradasLineas
    public void put(Placemark paradaKML, Folder lineaKML) {
        Linea linea = foldersYSuLinea.get(lineaKML);
        Parada parada = new Parada(paradaKML.getName(), getCoordenada(paradaKML));
        Parada paradaToAdd = null;
        if (!paradas.contains(parada)) {
            paradas.add(parada);
            paradaToAdd = parada;
        } else {
            Iterator<Parada> iterator = paradas.iterator();
            while (iterator.hasNext()) {
                Parada next = iterator.next();
                if (next.equals(parada)) {
                    paradaToAdd = next;
                    break;
                }
            }
        }
        paradaToAdd.getCorrespondencias().add(linea);
        linea.getParadas().add(paradaToAdd);
    }

    private static Coordenada getCoordenada(Placemark paradaKML) {
        double[] numericalCoordinates = paradaKML.getPoint().getNumericalCoordinates();
        Coordenada coordenada = new Coordenada(numericalCoordinates[0], numericalCoordinates[1]);
        return coordenada;
    }

    public PlanoMetro createPlanoMetro(String name) {
        List<Linea> lineas = new ArrayList<>();
        Iterator<Entry<Folder, Linea>> iterator = foldersYSuLinea.entrySet().iterator();
        while (iterator.hasNext()) {
            Linea lineaToAdd = iterator.next().getValue();
            lineas.add(lineaToAdd);
        }
        return new PlanoMetro(name, lineas, paradas);
    }
}
