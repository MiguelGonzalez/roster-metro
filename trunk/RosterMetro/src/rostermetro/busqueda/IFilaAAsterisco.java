/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda;

import java.util.LinkedList;
import java.util.List;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MAS_CORTA;
import static rostermetro.busqueda.BusquedaRuta.TipoRuta.MENOS_TRASBORDOS;
import rostermetro.domain.Parada;

/**
 * Interfaz para implementar la fila en la búsqueda con el algoritmo A*
 * @author Jaime Bárez y Miguel González
 */
public abstract class IFilaAAsterisco implements Comparable<IFilaAAsterisco> {
    
    protected Parada clave;
    protected IFilaAAsterisco anterior;
    protected Parada paradaFinal;
    protected final BusquedaRuta.TipoRuta tipoRuta;
    
    protected IFilaAAsterisco(Parada clave, IFilaAAsterisco anterior,
            Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
        this.tipoRuta = tipoRuta;
    }

    public static IFilaAAsterisco create(Parada clave, IFilaAAsterisco anterior, Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        switch (tipoRuta) {
            case MAS_CORTA:
                return new FilaAAsterisco(clave, anterior, paradaFinal, tipoRuta);

            case MENOS_TRASBORDOS:
                return new FilaAAsteriscoMenosTrasbordos(clave, anterior, paradaFinal, tipoRuta);
            default:
                return new FilaAAsterisco(clave, anterior, paradaFinal, BusquedaRuta.DEFAULT_TIPO_RUTA);
        }
    }
    
    public abstract double getH();
    public abstract double getG();
    public abstract double getF();
    
    public final Parada getClave() {
        return clave;
    }

    public final IFilaAAsterisco getAnterior() {
        return anterior;
    }
    
    /**
     * Devuelve los sucesores del nodo actual
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
     * @return the paradaFinal
     */
    public final Parada getParadaFinal() {
        return paradaFinal;
    }

    void setAnterior(IFilaAAsterisco filaATratar) {
        this.anterior = filaATratar;
    }
            
}
