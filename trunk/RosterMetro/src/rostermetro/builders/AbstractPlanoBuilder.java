package rostermetro.builders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.Plano;

/**
 * Clase usada por un PlanoParser para, a partir de clases externas, crear un
 * Plano (estructura conocida por el programa).
 *
 * @author Jaime Bárez y Miguel González
 * @param <P> Clase externa que representa una parada.
 * @param <L> Clase externa que representa una línea.
 */
public abstract class AbstractPlanoBuilder<P, L> {

    private final Map<L, Linea> externalLYSuLinea;
    private final HashSet<Parada> paradas;

    public AbstractPlanoBuilder() {
        externalLYSuLinea = new LinkedHashMap<>();
        paradas = new HashSet<>();
    }

    /**
     * El PlanoParser que use este PlanoBuilder irá llamando a este put al ir
     * recorriendo la estructura de datos externa, añadiendo las paradas en
     * orden con su línea correspondiente.
     *
     * @param paradaGeneric
     * @param lineaGeneric
     */
    public void put(P paradaGeneric, L lineaGeneric) {

        //Puede que esta línea ya haya sido registrada anteriormente
        Linea linea = externalLYSuLinea.get(lineaGeneric);
        if (linea == null) {
            linea = createLinea(lineaGeneric);
            externalLYSuLinea.put(lineaGeneric, linea);
        }

        Parada posibleParadaToAdd = createParada(paradaGeneric);
        Parada paradaToAdd = null;
        //Puedeque la parada ya haya sido registrada en otra línea
        if (paradas.contains(posibleParadaToAdd)) {//Gracias a sobreescribir el equals en l clase Parada
            //Iteramos hasta obtener la parada existente
            Iterator<Parada> iterator = paradas.iterator();
            while (iterator.hasNext()) {
                Parada next = iterator.next();
                if (next.equals(posibleParadaToAdd)) {
                    paradaToAdd = next;
                    break;
                }
            }
        } else {
            paradas.add(posibleParadaToAdd);
            paradaToAdd = posibleParadaToAdd;
        }
        //@jaimebarez método feo
        //Añadimos las corespondencias a la parada y la parada a la línea
        paradaToAdd.getCorrespondencias().add(linea);
        linea.getParadas().add(paradaToAdd);
    }

    /**
     * Creamos un plano con los datos que hemos ido añadiendo con la función put
     *
     * @param titulo
     * @return
     */
    public Plano createPlano(String titulo) {

        return new Plano(titulo, externalLYSuLinea.values(), paradas);
    }

    /**
     * Usado para limpiar los datos introducidos con la función put y reusar el
     * objeto
     */
    public void flush() {
        externalLYSuLinea.clear();
        paradas.clear();
    }

    /**
     * A sobreescribir por la clase que herede. Creará una línea a partir de la
     * línea proveída por una clase externa
     *
     * @param lineaGeneric
     * @return
     */
    protected abstract Linea createLinea(L lineaGeneric);

    /**
     * A sobreescribir por la clase que herede. Creará una parada a partir de la
     * parada proveída por una clase externa
     *
     * @param paradaGeneric
     * @return
     */
    protected abstract Parada createParada(P paradaGeneric);
}
