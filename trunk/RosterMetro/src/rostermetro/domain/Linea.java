package rostermetro.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Contiene un listado ordenado de paradas, puede ser circular si el nombre de
 * la línea acaba en "Circular".
 *
 * @author Jaime Bárez y Miguel González
 */
public class Linea {

    public static final String CIRCULAR = "Circular";
    private final String nombre;
    private final List<Parada> paradas;
    private final boolean circular;

    public Linea(String nombre) {
        this.nombre = nombre;
        this.paradas = new ArrayList<Parada>();
        circular = nombre.endsWith(CIRCULAR);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Parada> getParadas() {
        return Collections.unmodifiableList(paradas);
    }

    /**
     * Dada una parada, devuelve true si hay una parada anterior en esta línea
     *
     * @param parada
     * @return
     */
    private boolean tieneAnterior(Parada parada) {
        if (circular) {
            return true;
        } else {
            return paradas.indexOf(parada) > 0;
        }
    }

    /**
     * Devuelve la parada anterior a la dada en esta línea
     *
     * @param parada
     * @return
     */
    private Parada getAnteriorParada(Parada parada) {
        int index = paradas.indexOf(parada) - 1;
        if (circular && index < 0) {
            index = paradas.size() + index;
        }
        return paradas.get(index);
    }

    /**
     * Dada una parada, devuelve true si hay una parada siguiente en esta línea
     *
     * @param parada
     * @return
     */
    private boolean tieneSiguiente(Parada parada) {
        if (circular) {
            return true;
        } else {
            return paradas.indexOf(parada) != paradas.size() - 1;
        }
    }

    /**
     * Devuelve la parada siguiente a la dada en esta línea
     *
     * @param parada
     * @return
     */
    private Parada getSiguienteParada(Parada parada) {
        int index = paradas.indexOf(parada) + 1;
        if (circular && index >= paradas.size()) {
            index = index % paradas.size();
        }
        return paradas.get(index);

    }

    /**
     * Devuelve las paradas directamente conectadas a la dada en esta línea.
     *
     * @param parada
     * @return
     */
    public List<Parada> getParadasQueRodean(Parada parada) {
        List<Parada> sucesores = new LinkedList<Parada>();
        if (tieneAnterior(parada)) {
            sucesores.add(getAnteriorParada(parada));
        }
        if (tieneSiguiente(parada)) {
            sucesores.add(getSiguienteParada(parada));
        }
        return sucesores;
    }

    /**
     * Añada una parada al final de la línea
     *
     * @param paradaToAdd
     */
    public void addParada(Parada paradaToAdd) {
        paradas.add(paradaToAdd);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + nombre.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Linea other = (Linea) obj;
        if (!nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
