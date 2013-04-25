package rostermetro.busqueda.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import rostermetro.busqueda.BusquedaRuta;
import rostermetro.busqueda.FilaAAsterisco;
import rostermetro.busqueda.commons.Ruta;
import rostermetro.domain.Parada;

/**
 * Clase que se encarga de buscar la ruta entre dos paradas
 *
 * @author Jaime Bárez y Miguel González
 */
public class BusquedaRutaSimple extends BusquedaRuta<Ruta<Parada>>{

    @Override
    protected Ruta<Parada>  calcularRutaFinal() {
        return calcularFinal(abierta, paradaInicio);
    }

    public static Ruta<Parada>  calcularFinal(PriorityQueue<FilaAAsterisco> abierta, Parada paradaInicio) {
        FilaAAsterisco ultimaFila = abierta.peek();
        List<Parada> paradasRuta = new ArrayList<>();

        FilaAAsterisco aux = ultimaFila;
        //Recorremos, creando la ruta
        while (!aux.getClave().equals(paradaInicio)) {
            paradasRuta.add(aux.getClave());

            aux = aux.getAnterior();
        }
        paradasRuta.add(aux.getClave());
        //Damos la vuelta a la lista, ya quela hemos recorrido en sentido contrario
        Collections.reverse(paradasRuta);

        return new RutaSimple(paradasRuta);
    }

    private static class RutaSimple extends Ruta<Parada> {

        public RutaSimple(List<Parada> paradasRuta) {
            super(paradasRuta);
        }
    }
}
