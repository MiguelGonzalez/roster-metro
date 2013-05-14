package rostermetro.planoBuilders;

import java.util.HashSet;
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

    private Map<L, Linea> externalLYSuLinea;
    private HashSet<Parada> paradas;

    /**
     * Inicializa los valores internos que necesita para trabajar
     */
    public AbstractPlanoBuilder() {
        flush();
    }

    /**
     * El PlanoParser que use este PlanoBuilder irá llamando a este put al ir
     * recorriendo la estructura de datos externa, añadiendo las paradas en
     * orden con su línea correspondiente.
     *
     * @param paradaGeneric
     * @param lineaGeneric
     */
    public final void put(P paradaGeneric, L lineaGeneric) {

        //Puede que esta línea ya haya sido registrada anteriormente
        Linea linea = externalLYSuLinea.get(lineaGeneric);
        if (linea == null) {
            linea = createLinea(lineaGeneric);
            externalLYSuLinea.put(lineaGeneric, linea);
        }

        Parada posibleParadaToAdd = createParada(paradaGeneric);
        Parada paradaToAdd = null;
        //Puede que la parada ya haya sido registrada en otra línea
        if (paradas.contains(posibleParadaToAdd)) {//Gracias a sobreescribir el equals() en la clase Parada
            //Iteramos hasta obtener la parada existente a la que se apuntará
            for (Parada parada : paradas) {
                if (parada.equals(posibleParadaToAdd)) {
                    paradaToAdd = parada;
                    break;
                }
            }
        } else {//La parada no ha sido registrada aún
            paradas.add(posibleParadaToAdd);
            paradaToAdd = posibleParadaToAdd;
        }

        paradaToAdd.addCorrespondencia(linea);
        linea.addParada(paradaToAdd);
    }

    /**
     * Creamos un plano con los datos que hemos ido añadiendo con la función put
     *
     * @param titulo
     * @return
     */
    public final Plano createPlano(String titulo) {
        return new Plano(titulo, externalLYSuLinea.values(), paradas);
    }

    /**
     * Usado para limpiar los datos introducidos con la función put y reusar el
     * objeto
     */
    public final void flush() {
        externalLYSuLinea = new LinkedHashMap<>();
        paradas = new HashSet<>();
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
