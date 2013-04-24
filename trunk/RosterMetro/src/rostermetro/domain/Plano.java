package rostermetro.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Plano {

    private final String nombre;
    private final Collection<Linea> lineas;
    private final Set<Parada> paradas;

    public Plano(String nombre, Collection<Linea> lineas, Set<Parada> paradas) {
        this.nombre = nombre;
        this.lineas = Collections.unmodifiableCollection(lineas);
        this.paradas = Collections.unmodifiableSet(paradas);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the lineas
     */
    public Collection<Linea> getLineas() {
        return lineas;
    }

    /**
     * @return the paradas
     */
    public Collection<Parada> getParadas() {
        return paradas;
    }

    public Parada getParadaMasCercana(Coordenada coordenada) {
        Parada masCercana = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for (Parada parada : paradas) {
            double distancia = parada.getCoordenada().getHarversineDistanceTo(coordenada);
            if (distancia < menorDistancia) {
                masCercana = parada;
                menorDistancia = distancia;
            }
        }
        return masCercana;
    }

    public Parada getParada(String nombreParada) {
        Parada parada = null;
        Iterator<Parada> iterator = paradas.iterator();
        while (iterator.hasNext()) {
            Parada next = iterator.next();
            if (Objects.equals(nombreParada, next.getNombre())) {
                parada = next;
                break;
            }
        }
        return parada;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[[[");
        str.append(nombre);
        str.append("]]]").append("\n");
        for (Linea linea : getLineas()) {

            str.append("--------------");
            str.append(linea.toString());
            str.append("--------------").append("\n");
            for (Parada parada : linea.getParadas()) {
                str.append(parada.toString()).append("\n");
            }
        }
        return str.toString();
    }
    
}
