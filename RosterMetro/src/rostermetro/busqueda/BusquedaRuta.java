/**
 * Paquete dedicado a la búsqueda de rutas
 */
package rostermetro.busqueda;

import java.util.*;
import rostermetro.domain.*;

/**
 *
 * Clase que se encarga de buscar la ruta entre dos paradas
 *
 * @author Jaime Bárez y Miguel González
 * @param <R> Tipo de ruta
 */
public abstract class BusquedaRuta<R extends Ruta> {

    public static enum TipoRuta {

        MAS_CORTA, MENOS_TIEMPO, MENOS_TRASBORDOS;
    };
    public static final TipoRuta DEFAULT_TIPO_RUTA = TipoRuta.MAS_CORTA;
    //
    //
    private final HashMap<Parada, FilaAAsterisco> cerrada;
    protected final PriorityQueue<FilaAAsterisco> abierta;//Lista abierta ordenada
    protected final Parada paradaInicio;
    protected final Parada paradaFinal;

    public BusquedaRuta(Parada paradaInicio, Parada paradaFinal) {
        abierta = new PriorityQueue<>();
        cerrada = new HashMap<>();
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;
    }

    /**
     * Calcula la ruta por defecto
     *
     * @return
     */
    public R calcularRuta() {
        return calcularRuta(DEFAULT_TIPO_RUTA);
    }

    /**
     * Calcula la ruta dado el tipo de ruta a buscar
     *
     * @param tipoRuta
     * @return
     */
    public R calcularRuta(TipoRuta tipoRuta) {
        FilaAAsterisco filaInicial = FilaAAsterisco.create(paradaInicio, null, paradaFinal, tipoRuta);
        abierta.add(filaInicial);

        R rutaObtenida = calculaRutaRecursivo();

        abierta.clear();
        cerrada.clear();
        return rutaObtenida;
    }

    /**
     * LLamado por calcularRuta. Trabaja con una fila inicial en la lista
     * abierta.
     *
     * @return
     */
    private R calculaRutaRecursivo() {
        R calculada;
        if (abierta.isEmpty()) {
            calculada = null;//Not found
        } else if (abierta.peek().getClave().equals(paradaFinal)) {
            calculada = calcularRutaFinal();
        } else {
            FilaAAsterisco filaATratar = abierta.poll();
            cerrada.put(filaATratar.getClave(), filaATratar);
            //
            for (FilaAAsterisco sucesor : filaATratar.getSucesores()) {

                Parada sucesorClave = sucesor.getClave();
                FilaAAsterisco mismoEnCerrada= cerrada.get(sucesor.getClave());

                if (mismoEnCerrada!=null) {//Existe la clave en la lista cerrada
                    if (sucesor.compareTo(mismoEnCerrada) < 0) {
                        cerrada.remove(sucesorClave);
                        abierta.add(sucesor);//El sucesor tiene menor F que su anterior entrada en la lista cerrada
                    }
                }
                //El sucesor no estaba en la cerrada, lo añadimos a la abierta
                else  {
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
    protected R calcularRutaFinal(){
        FilaAAsterisco ultimaFila = abierta.peek();
        List<Parada> paradasRuta = new ArrayList<>();

        FilaAAsterisco aux = ultimaFila;
        //Recorremos, creando la ruta
        while (!aux.getClave().equals(paradaInicio)) {
            paradasRuta.add(aux.getClave());

            aux = aux.getAnterior();
        }
        paradasRuta.add(aux.getClave());
        //Damos la vuelta a la lista, ya que la hemos recorrido en sentido contrario
        Collections.reverse(paradasRuta);

        return getRfromList(paradasRuta);
    };
    protected abstract R getRfromList(List<Parada> paradasRuta);
}
