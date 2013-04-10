package rostermetro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import rostermetro.auxiliares.ADatosFilaAsterisco;
import rostermetro.domain.Parada;
import rostermetro.domain.Ruta;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class BusquedaRuta {

    private Parada paradaInicio;
    private Parada paradaFinal;
    private PriorityQueue<ADatosFilaAsterisco> abierta;

    public BusquedaRuta(Parada paradaInicio, Parada paradaFinal) {
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;

        abierta = new PriorityQueue<>();
    }

    public Ruta getRuta() {
        ADatosFilaAsterisco aDatosInicial = new ADatosFilaAsterisco(paradaInicio, null, paradaFinal);
        abierta.add(aDatosInicial);
        return getRutaRecursiva();
    }

    private Ruta getRutaRecursiva() {
        if (abierta.peek().getClave().equals(paradaFinal)) {
            //La mínima f es la parada final
            return calcularRutaFinal(abierta.peek());
        } else {

            ADatosFilaAsterisco aDatosCalcular = abierta.poll();

            List<ADatosFilaAsterisco> sucesores = aDatosCalcular.getSucesores();

            for (ADatosFilaAsterisco sucesor : sucesores) {
                abierta.add(sucesor);
            }

            return getRutaRecursiva();
        }
    }

    private Ruta calcularRutaFinal(ADatosFilaAsterisco aDatosFilaAsterisco) {
        List<Parada> paradasRuta = new ArrayList<>();

        ADatosFilaAsterisco aDatosFilaAsteriscoAux = aDatosFilaAsterisco;
        while (!aDatosFilaAsteriscoAux.getClave().equals(paradaInicio)) {
            paradasRuta.add(aDatosFilaAsteriscoAux.getClave());

            aDatosFilaAsteriscoAux = aDatosFilaAsteriscoAux.getAnterior();
        }
        paradasRuta.add(aDatosFilaAsteriscoAux.getClave());

        Collections.reverse(paradasRuta);

        return new Ruta(paradasRuta);
    }
}
