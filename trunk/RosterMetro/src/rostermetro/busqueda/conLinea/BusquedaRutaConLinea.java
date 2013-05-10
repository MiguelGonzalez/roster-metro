package rostermetro.busqueda.conLinea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import rostermetro.busqueda.BusquedaRuta;
import rostermetro.busqueda.Ruta;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 * Calcula la ruta con sus líneas mínimas
 *
 * @author Jaime Bárez y Miguel González
 */
public class BusquedaRutaConLinea extends BusquedaRuta<Ruta<ParadaRutaConLinea>> {

    public BusquedaRutaConLinea(Parada paradaInicio, Parada paradaFinal) {
        super(paradaInicio, paradaFinal);
    }

    /**
     * Esta función se encarga de calcular la ruta con su mínimo número de líneas para
     * recorrerla.
     * La técnica consiste en ir agregando a un saco todas las líneas con todas, si la línea
     * a agregar es distinta a la anterior se considera un transbordo. Al final de cada
     * parada se calcula el número de trasbordos mínimos y se eliminan aquellos
     * elementos del saco que superan ese número mínimo.
     * Al final en el saco quedan las rutas con líneas mínimas y se devuelve
     * la primera de todas ellas.
     * @param paradasRu Lista de paradas calculada
     * @return Ruta<ParadaRutaConLinea>
     */
    @Override
    protected Ruta<ParadaRutaConLinea> getRfromList(List<Parada> paradasRu) {
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

                Set<Linea> lineasQueLlevanSigParada = paradaActual.getLineasComunes(
                        paradaSiguiente);

                //Agregamos las líneas que nos llevan a la siguiente parada
                for (Linea linea : lineasQueLlevanSigParada) {
                    NodosLinea nodoLinea = new NodosLinea(linea);
                    listaParadasRutas.add(nodoLinea);
                }

            } else if (i == paradasRu.size() - 1) {
                if (listaParadasRutas.size() > 0) {
                    listaParadasRutas.get(0).addLinea(null);
                    lineasOptimasEncontradas = listaParadasRutas.get(0).getLineasTotales();
                }
            } else {
                Parada paradaSiguiente = paradasRu.listIterator(i).next();

                Set<Linea> lineasQueLlevanSigParada = paradaActual.getLineasComunes(paradaSiguiente);

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

            ParadaRutaConLinea paradaRuta = new ParadaRutaConLinea(paradaActual,
                    lineasOptimasEncontradas.get(i));
            paradasRuta.add(paradaRuta);
        }
        return new RutaConLinea(paradasRuta);
    }

    private class RutaConLinea extends Ruta<ParadaRutaConLinea> {

        public RutaConLinea(List<ParadaRutaConLinea> paradasRuta) {
            super(paradasRuta);
        }
    }

    /**
     * Objeto del saco para el algoritmo de búsqueda de ruta con líneas mínimas.
     */
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
