package rostermetro.busqueda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MAS_CORTA;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MENOS_TRASBORDOS;
import rostermetro.domain.Parada;

/**
 * Interfaz para implementar una fila en la búsqueda con el algoritmo AAsterisco
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class IFilaAAsterisco implements Comparable<IFilaAAsterisco> {

    protected final Parada clave;
    private final IFilaAAsterisco anterior;
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
     * Crea una IFilaAAsterisco a partir de una anterior. Si la anterior es null
     * es que es la primera
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
        List<IFilaAAsterisco> sucesores = new ArrayList<>();
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
        //Damos la vuelta a la lista, ya que la hemos recorrido en sentido contrario
        Collections.reverse(paradasRecorridas);
        return paradasRecorridas;
    }

    /**
     * A imlementar por la función que herede. Devolverá un peso respecto al
     * camino recorrido
     *
     * @return
     */
    public abstract double getH();

    /**
     * A implementar por la función que herede. Devolverá un peso estimado a la
     * parada final
     *
     * @return
     */
    public abstract double getG();

    /**
     * Devuelve la suma de G y H
     *
     * @return
     */
    public final double getF() {
        return getG() + getH();
    }

    /**
     * BusquedaRuta utiliza una PriorityQueue para ordenar las IFilaAAsterisco.
     * El orden se determina gracias a compareTo. La PriorityQueue extraerá
     * antes a los de menor F
     *
     * @param compareTo
     * @return
     */
    @Override
    public final int compareTo(IFilaAAsterisco compareTo) {
        return Double.compare(getF(), compareTo.getF());
    }

    /**
     * Devuelve la distancia a la parada final
     *
     * @return
     */
    protected final double getDistanciaAParadaFinal() {
        return clave.getDistancia(paradaFinal);
    }
}
