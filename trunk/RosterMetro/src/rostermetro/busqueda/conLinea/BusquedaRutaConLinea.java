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
     * Dada una lista de paradas, obtiene la ruta de paradas Y LINEAS. Llama a
     * getRfromListEstatico()
     *
     * @param paradasRu
     * @return
     */
    @Override
    protected Ruta<ParadaRutaConLinea> getRfromList(List<Parada> paradasRu) {
        return getRfromListEstatico(paradasRu);
    }

    /**
     * Esta función se encarga de calcular la ruta con su mínimo número de
     * líneas para recorrerla. La técnica consiste en ir agregando a un saco
     * todas las líneas con todas, si la línea a agregar es distinta a la
     * anterior se considera un transbordo. Al final de cada parada se calcula
     * el número de trasbordos mínimos y se eliminan aquellos elementos del saco
     * que superan ese número mínimo. Al final en el saco quedan las rutas con
     * líneas mínimas y se devuelve la primera de todas ellas.
     *
     * @param paradasRu Lista de paradas calculada
     * @return Ruta<ParadaRutaConLinea>
     */
    public static Ruta<ParadaRutaConLinea> getRfromListEstatico(List<Parada> paradasRu) {
        if (paradasRu == null) {
            return null;
        } else if (paradasRu.size() == 1) {
            return null;
        }

        //Guardamos la lista de líneas que se van sucediendo y su peso
        List<NodosLinea> listaParadasRutas = new ArrayList<>();
        List<Linea> lineasOptimasEncontradas = null;

        for (int i = 0; i < paradasRu.size(); i++) {
            Parada paradaActual = paradasRu.get(i);
            if (i == 0) {
                //Si es la primera parada
                Parada paradaSiguiente = paradasRu.get(i + 1);

                Set<Linea> lineasQueLlevanSigParada = paradaActual.getLineasComunes(
                        paradaSiguiente);

                //Agregamos las líneas que nos llevan a la siguiente parada
                for (Linea linea : lineasQueLlevanSigParada) {
                    NodosLinea nodoLinea = new NodosLinea(linea);
                    listaParadasRutas.add(nodoLinea);
                }

            } else if (i == paradasRu.size() - 1) {
                for (NodosLinea nodosLinea : listaParadasRutas) {
                    nodosLinea.addLinea(null);
                }
            } else {
                Parada paradaSiguiente = paradasRu.get(i + 1);

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
            List<NodosLinea> listaParadasRutasCopy =
                    new ArrayList<>(listaParadasRutas);
            Iterator<NodosLinea> itr = listaParadasRutasCopy.iterator();
            while (itr.hasNext()) {
                NodosLinea nodosLinea = itr.next();
                if (nodosLinea.getTransbordos() > transbordosMenor) {
                    listaParadasRutas.remove(nodosLinea);
                }
            }
        }

        if (listaParadasRutas.size() > 0) {
            lineasOptimasEncontradas = listaParadasRutas.get(0).getLineasTotales();
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
}
