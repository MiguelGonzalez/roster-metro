/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.ParadaRuta;

public class BusquedaRutaConLinea {
    private Ruta ruta;
    
    public BusquedaRutaConLinea(Ruta ruta) {
        this.ruta = ruta;
    }
    
    private class NodosLinea {
        private List<String> lineasRecorridas;
        private int numTransbordos;
        
        public NodosLinea(String nombreLineaParto) {
            lineasRecorridas = new ArrayList<>();
            lineasRecorridas.add(nombreLineaParto);
            numTransbordos = 0;
        }
        
        public NodosLinea(NodosLinea nodosLinea) {
            lineasRecorridas = new ArrayList<>(nodosLinea.getLineasTotales());
            numTransbordos = nodosLinea.getTransbordos();
        }
        
        public String getUltimaLinea() {
            return lineasRecorridas.get(lineasRecorridas.size() - 1);
        }
        
        public void addLinea(String linea) {
            if(!getUltimaLinea().equals(linea)) {
                numTransbordos++;
            }
            lineasRecorridas.add(linea);
        }
        
        public int getTransbordos() {
            return numTransbordos;
        }
        
        public List<String> getLineasTotales() {
            return lineasRecorridas;
        }
    }
    
    public List<ParadaRuta> getRutaConLinea() {
        //Guardamos la lista de líneas que se van sucediendo y su peso
        List<NodosLinea> listaParadasRutas = new ArrayList<>();
        List<String> lineasOptimasEncontradas = null;
        
        if(ruta == null) {
            return null;
        }
        
        List<Parada> paradas = ruta.getListadoParadas();
        
        if(paradas == null) {
            return null;
        }
        
        if(paradas.size() == 1) {
            Parada paradaActual = paradas.get(0);
            ParadaRuta paradaRuta = new ParadaRuta(paradaActual.getNombre(), paradaActual.getCoordenada(),
                    "El origen y destino es igual", false);
            List<ParadaRuta> paradasRuta = new ArrayList<ParadaRuta>();
            paradasRuta.add(paradaRuta);
            
            return paradasRuta;
        }
        
        for(int i=0; i<paradas.size(); i++) {
            Parada paradaActual = paradas.get(i);
            if(i==0) {
                //Si es la primera parada
                Parada paradaSiguiente;
                if(paradas.size() == 1) {
                    paradaSiguiente = paradaActual;
                } else {
                    paradaSiguiente = paradas.get(i + 1);
                }
                Set<Linea> lineasQueLlevanSigParada = lineaAlcanzaSiguienteParada(
                        paradaActual, paradaSiguiente);
                
                //Agregamos las líneas que nos llevan a la siguiente parada
                for(Linea linea : lineasQueLlevanSigParada) {
                    NodosLinea nodoLinea = new NodosLinea(linea.getNombre());
                    listaParadasRutas.add(nodoLinea);
                }
                
            } else if(i+1 == paradas.size()) {
                if(listaParadasRutas.size() > 0) {
                    listaParadasRutas.get(0).addLinea("Destino");
                    lineasOptimasEncontradas = listaParadasRutas.get(0).getLineasTotales();
                }
            } else {
                Parada paradaSiguiente = paradas.get(i+1);

                Set<Linea> lineasQueLlevanSigParada = lineaAlcanzaSiguienteParada(
                    paradaActual, paradaSiguiente);

                List<NodosLinea> listaParadasRutasSig = new ArrayList<>();
                
                for(NodosLinea nodosLinea : listaParadasRutas) {
                    //Para cada ruta agrego la línea
                    for(Linea lineaASigParada : lineasQueLlevanSigParada) {
                        NodosLinea nodosLineaCopy = new NodosLinea(nodosLinea);
                        nodosLineaCopy.addLinea(lineaASigParada.getNombre());
                        
                        listaParadasRutasSig.add(nodosLineaCopy);
                    }
                }
                
                listaParadasRutas.clear();
                listaParadasRutas.addAll(listaParadasRutasSig);
            }
            
            //Miramos cual es el mínimo de transbordos
            int transbordosMenor = -1;
            for(NodosLinea nodosLinea : listaParadasRutas) {
                int transbordos = nodosLinea.getTransbordos();
                if(transbordosMenor == -1 || transbordos < transbordosMenor) {
                    transbordosMenor = transbordos;
                }
            }
            
            //Eliminamos aquellas que superan el número de transbordos
            List<NodosLinea> listaParadasRutasCopyIterator = 
                    new ArrayList<>(listaParadasRutas);
            Iterator<NodosLinea> itr = listaParadasRutasCopyIterator.iterator();
            while (itr.hasNext()) {
                NodosLinea nodosLinea = itr.next();
                if(nodosLinea.getTransbordos() > transbordosMenor) {
                    listaParadasRutas.remove(nodosLinea);
                }
            }
        }
        
        if(lineasOptimasEncontradas == null) {
            return null;
        }
        
        List<ParadaRuta> paradasRuta = new ArrayList<>();
        
        for(int i=0; i<lineasOptimasEncontradas.size(); i++) {
            Parada paradaActual = paradas.get(i);

            boolean hayTransbordo = false;
            if(i < lineasOptimasEncontradas.size() - 1) {
                if(!paradaActual.getNombre().equals(paradas.get(i+1).getNombre())) {
                    hayTransbordo = true;
                }
            }

            ParadaRuta paradaRuta = new ParadaRuta(paradaActual.getNombre(), paradaActual.getCoordenada(),
                    lineasOptimasEncontradas.get(i), hayTransbordo);
            paradasRuta.add(paradaRuta);
        }
        
        
        
        return paradasRuta;
    }
    /*
    private boolean alcanzaSiguienteParada(Parada paradaOrigen, Parada paradaDestino) {
        Set<Parada> sucesores = paradaOrigen.getSucesores();
        boolean alcanzaSiguiente = false;
        for(Parada paradaSucesor : sucesores) {
            if(paradaSucesor.equals(paradaDestino)) {
                alcanzaSiguiente = true;
                break;
            }
        }
        
        return alcanzaSiguiente;
    }
    */
    private Set<Linea> lineaAlcanzaSiguienteParada(Parada paradaOrigen,
            Parada paradaDestino) {
        
        Set<Linea> lineasAlcance = paradaOrigen.getCorrespondencias();
        
        Set<Linea> lineasLlevanALaSiguienet = new HashSet<>();
        
        for(Linea linea : lineasAlcance) {
           List<Parada> paradasRodeanALaActual = linea.getParadasQueRodean(paradaOrigen);
           
           boolean lineaAlcanza = false;
           for(Parada paradaRodean : paradasRodeanALaActual) {
               if(paradaRodean.equals(paradaDestino)) {
                   lineaAlcanza = true;
               }
           }
           
           if(lineaAlcanza) {
               lineasLlevanALaSiguienet.add(linea);
           }
        }
        
        return lineasLlevanALaSiguienet;
    }
}
