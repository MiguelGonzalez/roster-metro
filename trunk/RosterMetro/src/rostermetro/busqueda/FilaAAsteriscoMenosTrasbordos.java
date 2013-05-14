package rostermetro.busqueda;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rostermetro.busqueda.conLinea.BusquedaRutaConLinea;
import rostermetro.busqueda.conLinea.ParadaRutaConLinea;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * IFilaAAsterisco para la búsqueda con menos trasbordos
 *
 * @author Jaime Bárez y Miguel González
 */
class FilaAAsteriscoMenosTrasbordos extends IFilaAAsterisco {

    /**
     * Peso que se le da a la distancia recorrida.
     */
    public static final double PONDERACION_DIST_RECORRIDA = 0.1d;
    /**
     * Peso que se la da a la distancia a la parada final.
     */
    public static final double PONDERACION_DIST_A_PARADAFINAL = 1d - PONDERACION_DIST_RECORRIDA;

    /**
     * Constructor protegido, ya que las filas se crean desde
     * IFilaAAsterisco.create()
     *
     * @param clave
     * @param anterior
     * @param paradaFinal
     * @param tipoRuta
     */
    protected FilaAAsteriscoMenosTrasbordos(Parada clave, IFilaAAsterisco anterior,
            Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        super(clave, anterior, paradaFinal, tipoRuta);
    }

    @Override
    protected FilaAAsteriscoMenosTrasbordos getAnterior() {
        return (FilaAAsteriscoMenosTrasbordos) super.getAnterior();
    }

    /**
     * Devuelve un double. Las unidades muestran el número que identifica el
     * número de trasbordos realizados. En las décimas muestra un número que va
     * desde [0 - 0.1) creciendo linealmente según crece la distancia real
     * recorrida
     *
     * @return
     */
    @Override
    public double getG() {
        /*La distancia mínima recorrida es 1. Si fuera menos, lo que va a 
         * la derecha de "getTrasbordos() +" podría dar negativo, y eso no puede ser*/
        double distRecorrida = Math.max(getDistanciaRecorrida(), 1d);
        return getTrasbordos() + PONDERACION_DIST_RECORRIDA * (1d - (1d / distRecorrida));
    }

    /**
     * Devuelve un double. A la izquierda de la coma siempre habrá un 0. En las
     * décimas muestra un número que va desde [0 - 0.9) creciendo linealmente
     * según aumenta la distancia al destino
     *
     * @return
     */
    @Override
    public double getH() {
        /*La distancia mínima a la parada final es 1. Si fuera menos, lo que va a 
         * la derecha de "getTrasbordos() +" podría dar negativo, y eso no puede ser*/
        double distAParadaFinal = Math.max(getDistanciaAParadaFinal(), 1d);
        return PONDERACION_DIST_A_PARADAFINAL * (1d - (1d / distAParadaFinal));
    }

    /**
     * Devuelve los trasbordos realizados hasta llegar a esta parada
     *
     * @return
     */
    public int getTrasbordos() {
        int trasbordos = 0;
        List<Parada> paradasRecorridas = getParadasRecorridas();

        Ruta<ParadaRutaConLinea> rutaConLineas = BusquedaRutaConLinea.getRfromListEstatico(paradasRecorridas);

        if (rutaConLineas != null) {
            //Para que no cuente su propia línea como trasbordo
            trasbordos--;
            List<ParadaRutaConLinea> listadoParadas = rutaConLineas.getListadoParadas();
            Set<Linea> setL = new HashSet<>();
            for (ParadaRutaConLinea paradaRutaConLinea : listadoParadas) {
                Linea linea = paradaRutaConLinea.getLinea();
                if (linea != null) {
                    //Si no estaba en el set, trasbordos++
                    if (setL.add(paradaRutaConLinea.getLinea())) {
                        trasbordos++;
                    }
                }
            }
        }
        return trasbordos;
    }
}
