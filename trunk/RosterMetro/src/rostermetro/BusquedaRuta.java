package rostermetro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import rostermetro.auxiliares.FilaAAsterisco;
import rostermetro.domain.Parada;
import rostermetro.domain.Ruta;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class BusquedaRuta {

    private Parada paradaInicio;
    private Parada paradaFinal;
    private PriorityQueue<FilaAAsterisco> abierta;//Lista abierta ordenada
    private final Set<FilaAAsterisco> cerrada;

    public BusquedaRuta(Parada paradaInicio, Parada paradaFinal) {
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;

        abierta = new PriorityQueue<>();
        cerrada = new HashSet<>();
    }

    public Ruta calcularRuta() {
        FilaAAsterisco filaInicial = new FilaAAsterisco(paradaInicio, null, paradaFinal);
        abierta.add(filaInicial);
        return calculaRutaRecursivo();
    }

    private Ruta calculaRutaRecursivo() {
        Ruta calculada;
        //System.out.println("Tope: " + abierta.peek().getClave());
        if(abierta.isEmpty()){
            calculada = null;//Not found
        }
        else if (abierta.peek().getClave().equals(paradaFinal)) {

            calculada = calcularRutaFinal(abierta.peek());
        } else {

            FilaAAsterisco aDatosCalcular = abierta.poll();
            cerrada.add(aDatosCalcular);
            //System.out.println(aDatosCalcular.getClave().getNombre());
            List<FilaAAsterisco> sucesores = aDatosCalcular.getSucesores();

            for (FilaAAsterisco sucesor : sucesores) {
                
                
                boolean existeClave = false;
                Iterator<FilaAAsterisco> iterator = cerrada.iterator();
                while(iterator.hasNext() && !existeClave){//Buscamos la clave en la lista de cerradas
                    
                    FilaAAsterisco nextFAsteriscoCerradas = iterator.next();
                    
                    if(Objects.equals(nextFAsteriscoCerradas.getClave(), sucesor.getClave())){//Existe la clave en la lista cerrada
                        if(sucesor.compareTo(nextFAsteriscoCerradas)<0){//La clave tiene menor F
                            System.out.println("actualizamos"
                                    + "");
                            cerrada.remove(nextFAsteriscoCerradas);
                            //TODO:probar con lineas circulares!!!
                            cerrada.add(sucesor);//Sustituimos
                        }
                        existeClave = true;
                    }
                }
                if(!existeClave){
                    abierta.add(sucesor);
                }
                
            }

            calculada = calculaRutaRecursivo();
        }
        return calculada;
    }

    private Ruta calcularRutaFinal(FilaAAsterisco ultimaFila) {
        List<Parada> paradasRuta = new ArrayList<>();

        FilaAAsterisco aux = ultimaFila;
        while (!aux.getClave().equals(paradaInicio)) {
            paradasRuta.add(aux.getClave());

            aux = aux.getAnterior();
        } 
        paradasRuta.add(aux.getClave());

        Collections.reverse(paradasRuta);

        return new Ruta(paradasRuta);
    }
}
