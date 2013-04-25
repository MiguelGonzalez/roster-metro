package rostermetro.busqueda.conLinea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.ArrayStack;
import rostermetro.busqueda.BusquedaRuta;
import rostermetro.busqueda.commons.Ruta;
import rostermetro.busqueda.simple.BusquedaRutaSimple;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

public class BusquedaRutaConLinea extends BusquedaRuta< Ruta<ParadaRutaConLinea>> {

    @Override
    protected Ruta<ParadaRutaConLinea> calcularRutaFinal() {
        return getRutaConLinea(BusquedaRutaSimple.calcularFinal(abierta, paradaInicio));
    }

    public Ruta<ParadaRutaConLinea> getRutaConLinea(Ruta ruta) {
        if (ruta == null) {
            return null;
        }
        List<Parada> paradasRu = ruta.getListadoParadas();
        if (paradasRu == null) {
            return null;
        }
        //Guardamos la lista de líneas que se van sucediendo y su peso
        List<NodosLinea> listaParadasRutas = new ArrayList<>();
        List<Linea> lineasOptimasEncontradas = null;
        
        for (int i = 0; i < paradasRu.size(); i++) {
            Parada paradaActual = paradasRu.get(i);
            if (i == 0) {
                //Si es la primera parada
                Parada paradaSiguiente = paradasRu.listIterator(i).next();

                Set<Linea> lineasQueLlevanSigParada = lineasAlcanzanSiguienteParada(
                        paradaActual, paradaSiguiente);

                //Agregamos las líneas que nos llevan a la siguiente parada
                for (Linea linea : lineasQueLlevanSigParada) {
                    NodosLinea nodoLinea = new NodosLinea(linea);
                    listaParadasRutas.add(nodoLinea);
                }

            } else if (i + 1 == paradasRu.size()) {
                if (listaParadasRutas.size() > 0) {
                    listaParadasRutas.get(0).addLinea(null);
                    lineasOptimasEncontradas = listaParadasRutas.get(0).getLineasTotales();
                }
            } else {
                Parada paradaSiguiente = paradasRu.listIterator(i).next();

                Set<Linea> lineasQueLlevanSigParada = lineasAlcanzanSiguienteParada(
                        paradaActual, paradaSiguiente);

                List<NodosLinea> listaParadasRutasSig = new ArrayList<>();

                for (NodosLinea nodosLinea : listaParadasRutas) {
                    //Para cada ruta agrego la línea
                    for (Linea lineaASigParada : lineasQueLlevanSigParada) {
                        NodosLinea nodosLineaCopy = new NodosLinea(nodosLinea);
                        nodosLineaCopy.addLinea(lineaASigParada);

                        listaParadasRutasSig.add(nodosLineaCopy);
                    }
                }

                listaParadasRutas.clear();
                listaParadasRutas.addAll(listaParadasRutasSig);
            }

            //Miramos cual es el mínimo de transbordos
            int transbordosMenor = -1;
            for (NodosLinea nodosLinea : listaParadasRutas) {
                int transbordos = nodosLinea.getTransbordos();
                if (transbordosMenor == -1 || transbordos < transbordosMenor) {
                    transbordosMenor = transbordos;
                }
            }

            //Eliminamos aquellas que superan el número de transbordos
            List<NodosLinea> listaParadasRutasCopyIterator =
                    new ArrayList<>(listaParadasRutas);
            Iterator<NodosLinea> itr = listaParadasRutasCopyIterator.iterator();
            while (itr.hasNext()) {
                NodosLinea nodosLinea = itr.next();
                if (nodosLinea.getTransbordos() > transbordosMenor) {
                    listaParadasRutas.remove(nodosLinea);
                }
            }
        }

        if (lineasOptimasEncontradas == null) {
            return null;
        }

        List<ParadaRutaConLinea> paradasRuta = new ArrayList<>();

        for (int i = 0; i < lineasOptimasEncontradas.size(); i++) {
            Parada paradaActual = paradasRu.get(i);


            ParadaRutaConLinea paradaRuta = new ParadaRutaConLinea(paradaActual.getNombre(), paradaActual.getCoordenada(),
                    lineasOptimasEncontradas.get(i));
            paradasRuta.add(paradaRuta);
        }
        return new RutaConLinea(paradasRuta);
    }

    private Set<Linea> lineasAlcanzanSiguienteParada(Parada paradaOrigen,
            Parada paradaDestino) {

        Set<Linea> lineasAlcanzanSiguienteParada = new HashSet<>(paradaOrigen.getCorrespondencias());
        lineasAlcanzanSiguienteParada.retainAll(paradaDestino.getCorrespondencias());
        return lineasAlcanzanSiguienteParada;
    }

    private class RutaConLinea extends Ruta<ParadaRutaConLinea> {

        public RutaConLinea(List<ParadaRutaConLinea> paradasRuta) {
            super(paradasRuta);
        }
    }

    private class NodosLinea {

        private List<Linea> lineasRecorridas;
        private int numTransbordos;

        public NodosLinea(Linea miLinea) {
            lineasRecorridas = new ArrayList<>();
            lineasRecorridas.add(miLinea);
            numTransbordos = 0;
        }

        public NodosLinea(NodosLinea nodosLinea) {
            lineasRecorridas = new ArrayList<>(nodosLinea.getLineasTotales());
            numTransbordos = nodosLinea.getTransbordos();
        }

        public Linea getUltimaLinea() {
            return lineasRecorridas.get(lineasRecorridas.size() - 1);
        }

        public void addLinea(Linea linea) {
            if (!getUltimaLinea().equals(linea)) {
                numTransbordos++;
            }
            lineasRecorridas.add(linea);
        }

        public int getTransbordos() {
            return numTransbordos;
        }

        public List<Linea> getLineasTotales() {
            return lineasRecorridas;
        }
    }
}
