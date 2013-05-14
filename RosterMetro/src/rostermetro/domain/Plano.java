package rostermetro.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Representa toda la red de líneas y paradas.
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

    public String getNombre() {
        return nombre;
    }

    public Collection<Linea> getLineas() {
        return lineas;
    }

    public Collection<Parada> getParadas() {
        return paradas;
    }

    /**
     * Dada una coordenada, devuelve la parada más cercana a la misma
     *
     * @param coordenada
     * @return
     */
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

    /**
     * Dado el principio del nombre de parada devuelve el primero objeto
     * encontrado que lo representa en este plano (si existe). No importa la
     * capitalización de las letras, aunque sí las tildes
     *
     * @param empiezaPor
     * @return
     */
    public Parada getParada(String empiezaPor) {
        empiezaPor = empiezaPor.toLowerCase();
        Parada parada = null;

        for (Parada next : paradas) {
            if ((next.getNombre()).toLowerCase().startsWith(empiezaPor)) {
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
