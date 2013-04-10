package rostermetro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import rostermetro.auxiliares.ADatosFilaAsterisco;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.Ruta;

public class BusquedaRuta {
    private Parada paradaInicio;
    private Parada paradaFinal;
    private PriorityQueue<ADatosFilaAsterisco> abierta;
    private List<ADatosFilaAsterisco> cerrada;
    
    private Ruta rutaCalculada;

    public BusquedaRuta(Parada paradaInicio, Parada paradaFinal) {
        this.paradaInicio = paradaInicio;
        this.paradaFinal = paradaFinal;
        
        abierta = new PriorityQueue<>();
        cerrada = new ArrayList<>();
    }
    
    public Ruta getRuta() {
      /*  ArrayList<Parada> sucesores = new ArrayList<>();
        for (Linea lineaIntersecta : paradaInicio.getCorrespondencias()) {
            sucesores.addAll(lineaIntersecta.getSucesores(paradaFinal));
        }
        */
        
        ADatosFilaAsterisco aDatosInicial = new ADatosFilaAsterisco(paradaInicio, null, paradaFinal);
        abierta.add(aDatosInicial);
        
        
        getRutaRecursiva();
        
        return rutaCalculada;
    }
    
    private void getRutaRecursiva() {
        if(abierta.peek().getClave().equals(paradaFinal)) {
            
            //Calcular  rutaCalculada
            calcularRutaFinal(abierta.peek());
        } else {
           
            ADatosFilaAsterisco aDatosCalcular = abierta.poll();
            cerrada.add(aDatosCalcular);
            
            List<Parada> sucesores = aDatosCalcular.getSucesores();
            
            for(Parada parada : sucesores) {
                ADatosFilaAsterisco aDatosFilaAsterisco = new
                        ADatosFilaAsterisco(parada, aDatosCalcular, paradaFinal);
                
                abierta.add(aDatosFilaAsterisco);
            }
            
            getRutaRecursiva();
        }
    }
    
    private void calcularRutaFinal(ADatosFilaAsterisco aDatosFilaAsterisco) {
        List<Parada> paradasRuta = new ArrayList<Parada>();
        
        ADatosFilaAsterisco aDatosFilaAsteriscoAux = aDatosFilaAsterisco;
        while(! aDatosFilaAsteriscoAux.getClave().equals(paradaInicio)) {
            paradasRuta.add(aDatosFilaAsteriscoAux.getClave());
            
            aDatosFilaAsteriscoAux = aDatosFilaAsteriscoAux.getAnterior();
        }
        paradasRuta.add(aDatosFilaAsteriscoAux.getClave());
        
        Collections.reverse(paradasRuta);
        
        rutaCalculada = new Ruta(paradasRuta);
    }
    
    private void funcrecusiva() {
         
    }
    
    
  
}
