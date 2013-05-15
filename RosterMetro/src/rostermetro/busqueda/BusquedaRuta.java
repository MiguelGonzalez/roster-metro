package rostermetro.busqueda;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import rostermetro.domain.Parada;

/**
 *
 * Clase que se encarga de buscar la ruta entre dos paradas
 *
 * @author Jaime Bárez y Miguel González
 * @param <R> Tipo de ruta
 */
public abstract class BusquedaRuta<R extends Ruta> {

    

    public static enum TipoRuta {

        MAS_CORTA, MENOS_TRASBORDOS;
    };
    private final HashMap<Parada, IFilaAAsterisco> cerrada;
    protected final PriorityQueue<IFilaAAsterisco> abierta;//Lista abierta ordenada
    protected final Parada paradaInicio;
    protected final Parada paradaFinal;
    private int nodosExplorados;

    /**
     * Construye el objeto e inicializa las variables
     *
     * @param paradaInicio Recibe la parada de origen
     * @param paradaFinal Recibe la parada de destino
     */
    public BusquedaRuta(Parada paradaInicio, Parada paradaFinal) {
        abierta = new PriorityQueue<>();
        cerrada = new HashMap<>();
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;
        nodosExplorados = 0;
    }
    
    /**
     * @return the nodosExplorados
     */
    public int getNodosExplorados() {
        return nodosExplorados;
    }

    /**
     * Calcula la ruta dado el tipo de ruta a buscar
     *
     * @param tipoRuta
     * @return R La ruta calculada (null si no existe ruta)
     */
    public R calcularRuta(TipoRuta tipoRuta) {
        nodosExplorados =0;
        IFilaAAsterisco filaInicial = IFilaAAsterisco.create(paradaInicio, null, paradaFinal, tipoRuta);

        abierta.add(filaInicial);

        R rutaObtenida = calculaRutaRecursivo();

        //Limpia las listas para un posible reuso del objeto
        abierta.clear();
        cerrada.clear();
        return rutaObtenida;
    }

    /**
     * LLamado por calcularRuta. Se va llamando recursivamente hasta que
     * encuentra la parada final en la cima de la lista abierta
     *
     * @return R La ruta calculada (null si no existe ruta)
     */
    private R calculaRutaRecursivo() {
        R calculada;
        if (abierta.isEmpty()) {
            calculada = null;//Not found
        } else if (abierta.peek().getClave().equals(paradaFinal)) {
            calculada = calcularRutaFinal();
        } else {
            nodosExplorados++;
            IFilaAAsterisco filaATratar = abierta.poll();
            cerrada.put(filaATratar.getClave(), filaATratar);

            //Recorremos todos los nodos sucesores
            for (IFilaAAsterisco sucesor : filaATratar.getSucesores()) {
                Parada sucesorClave = sucesor.getClave();
                IFilaAAsterisco mismoEnCerrada = cerrada.get(sucesor.getClave());

                //Si el sucesor está en la lista cerrada...
                if (mismoEnCerrada != null) {
                    int compareTo = Double.compare(sucesor.getG(), mismoEnCerrada.getG());
                    //Y tiene menor G que el de la cerrada...
                    if (compareTo < 0) {
                        //Actualizamos la entrada
                        cerrada.remove(sucesorClave);
                        abierta.add(sucesor);
                    }
                } else {
                    /*Si no está en la lista cerrada es que aún no ha sido recorrido.
                     * Lo añadimos a la abierta*/
                    abierta.add(sucesor);
                }
            }

            //Seguimos recorriendo
            calculada = calculaRutaRecursivo();
        }
        return calculada;
    }

    /**
     * Llamada por calculaRutaRecursivo() una vez hemos encontrado la ruta
     * final. Va creando la lista de paradas, desapilando la parada con menos F
     * de la lista y obteniendo su anterior, así una a una
     *
     * @return R La ruta calculada (null si no existe ruta)
     */
    private R calcularRutaFinal() {
        IFilaAAsterisco ultimaFila = abierta.peek();
        List<Parada> paradasRuta = ultimaFila.getParadasRecorridas();

        return getRfromList(paradasRuta);
    }

    /**
     * Se llama por calcularRutaFinal para devolver el tipo de ruta que la clase
     * que herede quiera, gracias al uso de genéricos (R)
     *
     * @param paradasRuta
     * @return R La ruta calculada (null si no existe ruta)
     */
    protected abstract R getRfromList(List<Parada> paradasRuta);
}
