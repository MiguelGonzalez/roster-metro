package rostermetro.busqueda;

import rostermetro.busqueda.BusquedaRuta.TipoRuta;
import rostermetro.domain.Parada;

/**
 *
 * IFilaAAsterisco para la búsqueda más corta
 *
 * @author Jaime Bárez y Miguel González
 */
public class FilaAAsteriscoMasCorta extends IFilaAAsterisco {

    /**
     * Constructor protegido, ya que las filas se crean desde
     * IFilaAAsterisco.create()
     *
     * @param clave
     * @param anterior
     * @param paradaFinal
     * @param tipoRuta
     */
    protected FilaAAsteriscoMasCorta(Parada clave, IFilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        super(clave, anterior, paradaFinal, tipoRuta);
    }

    @Override
    protected FilaAAsteriscoMasCorta getAnterior() {
        return (FilaAAsteriscoMasCorta) super.getAnterior();
    }

    /**
     * Devuelve la distancia en metros desde la parada de esta fila a la parada
     * final
     *
     * @return
     */
    @Override
    public double getH() {
        return super.getDistanciaAParadaFinal();
    }

    /**
     * Devuelve la distancia real recorrida hasta la parada de esta fila
     *
     * @return
     */
    @Override
    public double getG() {
        return getDistanciaRecorrida();
    }
}
