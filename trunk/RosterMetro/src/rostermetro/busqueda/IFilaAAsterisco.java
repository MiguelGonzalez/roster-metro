package rostermetro.busqueda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MAS_CORTA;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MENOS_TRASBORDOS;
import rostermetro.domain.Parada;

/**
 * Interfaz para implementar la fila en la búsqueda con el algoritmo AAsterisco
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class IFilaAAsterisco implements Comparable<IFilaAAsterisco> {

    protected final Parada clave;
    private IFilaAAsterisco anterior;
    private final Parada paradaFinal;
    protected final BusquedaRuta.TipoRuta tipoRuta;

    /**
     * Se inicializan las variables
     *
     * @param clave
     * @param anterior
     * @param paradaFinal
     * @param tipoRuta
     */
    protected IFilaAAsterisco(Parada clave, IFilaAAsterisco anterior,
            Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
        this.tipoRuta = tipoRuta;
    }

    /**
     * Crea una IFilaAAsterisco a partir de una anterior
     *
     * @param clave
     * @param anterior
     * @param paradaFinal
     * @param tipoRuta
     * @return
     */
    public static IFilaAAsterisco create(Parada clave, IFilaAAsterisco anterior, Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        switch (tipoRuta) {
            case MAS_CORTA:
                return new FilaAAsteriscoMasCorta(clave, anterior, paradaFinal, tipoRuta);

            case MENOS_TRASBORDOS:
                return new FilaAAsteriscoMenosTrasbordos(clave, anterior, paradaFinal, tipoRuta);
            default:
                return null;
        }
    }

    /**
     *
     * @return la clave(Parada)de esta fila
     */
    public final Parada getClave() {
        return clave;
    }

    /**
     * Devuelve la IFilaAAsterisco anterior o null si no tiene anterior
     *
     * @return
     */
    protected IFilaAAsterisco getAnterior() {
        return anterior;
    }

    /**
     * Devuelve los sucesores de la fila actual
     *
     * @return List<FilaAAsterisco>
     */
    public final List<IFilaAAsterisco> getSucesores() {
        List<IFilaAAsterisco> sucesores = new LinkedList<>();
        for (Parada parada : clave.getSucesores()) {
            IFilaAAsterisco sucesor = create(parada, this, getParadaFinal(), tipoRuta);
            sucesores.add(sucesor);
        }
        return sucesores;
    }

    /**
     * @return La parada final
     */
    public final Parada getParadaFinal() {
        return paradaFinal;
    }

    /**
     * Actualiza la filaAAsterisco anterior. Puede ser útil cuando se ha
     * encontrado un camino mejor para llegar a esta fila
     *
     * @param anterior
     */
    protected final void setAnterior(IFilaAAsterisco anterior) {
        this.anterior = anterior;
    }

    /**
     * Devuelve la distancia recorrida desde el inicio hasta esta fila
     *
     * @return
     */
    protected final double getDistanciaRecorrida() {
        if (anterior == null) {
            return 0;
        } else {
            return anterior.getDistanciaRecorrida() + clave.getDistancia(anterior.getClave());
        }
    }

    /**
     * Devuelve una lista de paradas recorridas
     *
     * @return
     */
    public final List<Parada> getParadasRecorridas() {
        List<Parada> paradasRecorridas = new ArrayList<>();

        IFilaAAsterisco ant = this;
        while (ant != null) {
            paradasRecorridas.add(ant.getClave());
            ant = ant.getAnterior();
        }
        return paradasRecorridas;
    }

    public abstract double getH();

    public abstract double getG();

    public abstract double getF();

    /**
     * BusquedaRuta utiliza una PriorityQueue para ordenar las IFilaAAsterisco.
     * El orden se determina gracias a compareTo
     *
     * @param compareTo
     * @return
     */
    @Override
    public final int compareTo(IFilaAAsterisco compareTo) {
        return Double.compare(getF(), compareTo.getF());
    }
}
