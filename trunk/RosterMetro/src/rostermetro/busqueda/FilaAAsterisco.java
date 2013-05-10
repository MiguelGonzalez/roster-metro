package rostermetro.busqueda;

import rostermetro.busqueda.BusquedaRuta.TipoRuta;
import rostermetro.domain.Parada;

/**
 * Fila AAsterisco
 *
 * Clase que posee los datos de una fila del algoritmo A*.
 *
 * @author Jaime Bárez y Miguel González
 */
public class FilaAAsterisco extends IFilaAAsterisco {

    protected FilaAAsterisco(Parada clave, IFilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        super(clave, anterior, paradaFinal, tipoRuta);
    }

    @Override
    public double getH() {
        return clave.getDistancia(getParadaFinal());
    }

    @Override
    public double getG() {
        if (anterior == null) {
            return 0;
        } else {
            return anterior.getG() + clave.getDistancia(anterior.getClave());
        }
    }

    @Override
    public double getF() {
        return getH() + getG();
    }

    @Override
    public int compareTo(IFilaAAsterisco compareTo) {
        return Double.compare(getF(), compareTo.getF());
    }
}
