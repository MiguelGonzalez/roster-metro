package rostermetro.busqueda;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import rostermetro.busqueda.commons.Ruta;
import rostermetro.domain.Parada;

/**
 * Clase que se encarga de buscar la ruta entre dos paradas
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class BusquedaRuta<T extends Ruta>{

    public static enum TipoRuta {

        MAS_CORTA, MAS_LARGA;
    };
    public static final TipoRuta DEFAULT_TIPO_RUTA = TipoRuta.MAS_CORTA;
    protected final PriorityQueue<FilaAAsterisco> abierta;//Lista abierta ordenada
    private final Set<FilaAAsterisco> cerrada;
    protected Parada paradaInicio;
    protected Parada paradaFinal;

    public BusquedaRuta() {
        abierta = new PriorityQueue<>();
        cerrada = new HashSet<>();
    }

    public T calcularRuta(Parada paradaInicio, Parada paradaFinal) {
        return calcularRuta(paradaInicio, paradaFinal, DEFAULT_TIPO_RUTA);
    }

    /**
     * Calcula la ruta dados unos parámetros necesarios de entrada
     *
     * @param paradaInicio
     * @param paradaFinal
     * @param tipoRuta
     * @return la ruta entre las dos paradas. null si no existe (líneas o
     * paradas aisladas).
     */
    public T calcularRuta(Parada paradaInicio, Parada paradaFinal, TipoRuta tipoRuta) {
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;
        abierta.clear();
        cerrada.clear();
        FilaAAsterisco filaInicial = FilaAAsterisco.create(paradaInicio, null, paradaFinal, tipoRuta);
        abierta.add(filaInicial);

        T rutaObtenida=  calculaRutaRecursivo();
        //@jaimebarez
        return rutaObtenida;
    }

    /**
     * LLamado por calcularRuta. Trabaja con una fila inicial en la lista
     * abierta.
     *
     * @return
     */
    private T calculaRutaRecursivo() {
        T calculada;
        if (abierta.isEmpty()) {
            calculada = null;//Not found
        } else if (abierta.peek().getClave().equals(paradaFinal)) {
            calculada = calcularRutaFinal();
        } else {

            FilaAAsterisco filaATratar = abierta.poll();
            cerrada.add(filaATratar);

            for (FilaAAsterisco sucesor : filaATratar.getSucesores()) {

                //No añadiremos el sucesor a la lista abierta si ya está en la lista cerrada, a no ser que tenga menor F
                boolean sucesorEnCerrada = false;
                Iterator<FilaAAsterisco> cerradaIterator = cerrada.iterator();
                while (cerradaIterator.hasNext() && !sucesorEnCerrada) {//Buscamos la clave en la lista de cerradas

                    FilaAAsterisco nextFAsteriscoCerradas = cerradaIterator.next();

                    if (Objects.equals(nextFAsteriscoCerradas.getClave(), sucesor.getClave())) {//Existe la clave en la lista cerrada
                        if (sucesor.compareTo(nextFAsteriscoCerradas) < 0) {
                            cerrada.remove(nextFAsteriscoCerradas);
                            abierta.add(sucesor);//El sucesor tiene menor F que su anterior entrada en la lista cerrada
                        }
                        sucesorEnCerrada = true;


                    }
                }//Fin while
                //El sucesor no estaba en la cerrada, lo añadimos a la abierta
                if (!sucesorEnCerrada) {
                    abierta.add(sucesor);
                }
            }
            //Seguimos recorriendo
            calculada = calculaRutaRecursivo();
        }
        return calculada;
    }

    /**
     * Llamada por calculaRutaRecursivo() una vez hemos encontrado la ruta final
     *
     * @return
     */
    protected abstract T calcularRutaFinal();
}