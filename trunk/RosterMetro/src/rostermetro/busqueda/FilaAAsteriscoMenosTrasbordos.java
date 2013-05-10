/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rostermetro.busqueda.conLinea.BusquedaRutaConLinea;
import rostermetro.busqueda.conLinea.ParadaRutaConLinea;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * @author ceura
 */
class FilaAAsteriscoMenosTrasbordos extends IFilaAAsterisco {

    public FilaAAsteriscoMenosTrasbordos(Parada clave, IFilaAAsterisco anterior,
            Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        super(clave, anterior, paradaFinal, tipoRuta);
    }

    @Override
    public double getG() {
        return getTrasbordos(false);
    }

    @Override
    public double getH() {
        if (anterior == null) {
            return 0;
        } else {
            return ((FilaAAsteriscoMenosTrasbordos)anterior).getH() + clave.getDistancia(anterior.getClave());
        }
    }

    @Override
    public double getF() {
        //System.out.println(getG());
        /*
         * Le damos un peso mínimo a la distancia para discriminar
         * Rutas con el mismo número de transbordos que se están
         * Alejando del destino final.
         * Objetivo
         * Acercar el resultado al menor número de trasbordos y la ruta
         * más cercana.
        */ 
        return getG() + (getH() / 1000000000d);
    }
  
    @Override
    public int compareTo(IFilaAAsterisco compareTo) {
        return Double.compare(getF(), ((FilaAAsteriscoMenosTrasbordos)compareTo).getF());
    }
    
    

    public int getTrasbordos(boolean mostrarMensaje) {
        List<Parada> paradasPasado = new ArrayList<>();
        
        IFilaAAsterisco ant = this;
        while(ant!=null){
            paradasPasado.add(ant.getClave());
            ant = ant.getAnterior();
        }
        Set<Linea> setL = new HashSet<>();
        List<ParadaRutaConLinea> listadoParadas;
        try {
        listadoParadas = BusquedaRutaConLinea.getRfromListEstatico
                (paradasPasado).getListadoParadas();
        } catch(NullPointerException ex) {
            return 0;
        }
        
        
       for(ParadaRutaConLinea paradaRutaConLinea :listadoParadas){
           
           setL.add(paradaRutaConLinea.getLinea());
       }
       return setL.size();
        
    }
}
