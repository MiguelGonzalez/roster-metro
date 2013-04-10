package rostermetro.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import rostermetro.BusquedaRuta;
import rostermetro.Utilidades;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoMetro {

    private final String nombre;
    private final Collection<Linea> lineas;
    private final Set<Parada> paradas;

    PlanoMetro(String nombre, Collection<Linea> lineas, Set<Parada> paradas) {
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Utilidades.appendLine(str, nombre);
        for (Linea linea : getLineas()) {
            Utilidades.appendLine(str, linea.toString());
        }
        return str.toString();
    }

    public Parada getParadaMasCercana(Coordenada coordenada) {
        Parada masCercana = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for (Parada parada : paradas) {
            double distancia = parada.getCoordenada().getDistanceTo(coordenada);
            if (distancia < menorDistancia) {
                masCercana = parada;
                menorDistancia = distancia;
            }
        }
        return masCercana;
    }
    public Parada getParada(String nombreParada){
        Parada parada = null;
        Iterator<Parada> iterator = paradas.iterator();
        while(iterator.hasNext()){
            Parada next = iterator.next();
            if(Objects.equals(nombreParada, next.getNombre())){
                parada = next;
                break;
            }
        }
        return parada;
    }
    
    public Ruta getRuta(Parada paraInicio, Parada paraFinal) {
        BusquedaRuta busquedaRuta = new BusquedaRuta(paraInicio, paraFinal);
        
        return busquedaRuta.getRuta();
    }
}
