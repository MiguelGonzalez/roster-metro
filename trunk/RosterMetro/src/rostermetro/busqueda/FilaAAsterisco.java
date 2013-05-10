package rostermetro.busqueda;

import java.util.LinkedList;
import java.util.List;
import rostermetro.busqueda.BusquedaRuta.TipoRuta;
import rostermetro.domain.Parada;

/**
 * Fila A
 *
 *
 * @author Jaime Bárez y Miguel González
 */
public class FilaAAsterisco implements Comparable<FilaAAsterisco> {

    private Parada clave;
    private FilaAAsterisco anterior;
    private Parada paradaFinal;
    private final TipoRuta tipoRuta;

    protected FilaAAsterisco(Parada clave, FilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
        this.tipoRuta = tipoRuta;
    }

    public double getH() {
        return clave.getDistancia(getParadaFinal());
    }

    public double getG() {
        if (anterior == null) {
            return 0;
        } else {
            return anterior.getG() + clave.getDistancia(anterior.getClave());
        }
    }

    public double getF() {
        return getH() + getG();
    }

    public Parada getClave() {
        return clave;
    }

    public FilaAAsterisco getAnterior() {
        return anterior;
    }

    public List<FilaAAsterisco> getSucesores() {
        List<FilaAAsterisco> sucesores = new LinkedList<>();
        for (Parada parada : clave.getSucesores()) {
            FilaAAsterisco sucesor = create(parada, this, getParadaFinal(), tipoRuta);
            sucesores.add(sucesor);
        }
        return sucesores;
    }

    @Override
    public int compareTo(FilaAAsterisco compareTo) {
        return Double.compare(getF(), compareTo.getF());
    }

    public static FilaAAsterisco create(Parada clave, FilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        switch (tipoRuta) {
            case MAS_CORTA:
                return new FilaAAsterisco(clave, anterior, paradaFinal, tipoRuta);

            case MENOS_TRASBORDOS:
                return new FilaAAsteriscoMenosTrasbordos(clave, anterior, paradaFinal, tipoRuta);
            default:
                return new FilaAAsterisco(clave, anterior, paradaFinal, BusquedaRuta.DEFAULT_TIPO_RUTA);
        }
    }

    /**
     * @return the paradaFinal
     */
    protected Parada getParadaFinal() {
        return paradaFinal;
    }
}
