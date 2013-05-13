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
 * IFilaAAsterisco para la búsqueda más con menos trasbordos
 *
 * @author Jaime Bárez y Miguel González
 */
class FilaAAsteriscoMenosTrasbordos extends IFilaAAsterisco {

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
     * Devuelve el número de trasbordos
     *
     * @return
     */
    @Override
    public double getG() {
        return getTrasbordos();
    }

    /**
     * Devuelve la distancia recorrida
     *
     * @return
     */
    @Override
    public double getH() {
        return getDistanciaRecorrida();
    }

    @Override
    public double getF() {

        //System.out.println(getG());
        /*
         * Le damos un peso mínimo a la distancia para discriminar
         * Rutas con el mismo número de transbordos que se están
         * Alejando del destino final.
         * Objetivo
         * Acercar el resultado al menor número de trasbordos y la ruta
         * más cercana.
         */
        //@MIRAR
        if (clave.getNombre().toLowerCase().startsWith("villa")) {
            System.out.println("");
        }

        return (getG() * 10) + (1d - (1d / getH()));
    }

    public int getTrasbordos() {
        int trasbordos = 0;
        List<Parada> paradasRecorridas = getParadasRecorridas();

        Ruta<ParadaRutaConLinea> rutaConLineas = BusquedaRutaConLinea.getRfromListEstatico(paradasRecorridas);

        if (rutaConLineas != null) {

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
