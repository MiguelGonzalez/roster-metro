package rostermetro.busqueda;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import rostermetro.busqueda.BusquedaRuta.TipoRuta;
import rostermetro.busqueda.conLinea.BusquedaRutaConLinea;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class FilaAAsterisco implements Comparable<FilaAAsterisco> {

    private Parada clave;
    protected FilaAAsterisco anterior;
    private Parada paradaFinal;
    private final TipoRuta tipoRuta;

    private FilaAAsterisco(Parada clave, FilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
        this.tipoRuta = tipoRuta;
    }

    public double getH() {
        return clave.getDistancia(paradaFinal);
    }

    public double getG() {
        if (anterior == null) {
            return 0;
        } else {
            return anterior.getG() + clave.getDistancia(anterior.getClave());
        }
    }

    public double getF() {
        return getH() + getG();
    }

    public Parada getClave() {
        return clave;
    }

    public FilaAAsterisco getAnterior() {
        return anterior;
    }

    public List<FilaAAsterisco> getSucesores() {
        List<FilaAAsterisco> sucesores = new LinkedList<>();
        for (Parada parada : clave.getSucesores()) {
            FilaAAsterisco sucesor = create(parada, this, paradaFinal, tipoRuta);
            sucesores.add(sucesor);
        }
        return sucesores;
    }

    @Override
    public int compareTo(FilaAAsterisco compareTo) {
        double result = getF() - compareTo.getF();
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static FilaAAsterisco create(Parada clave, FilaAAsterisco anterior, Parada paradaFinal, TipoRuta tipoRuta) {
        switch (tipoRuta) {
            case MAS_CORTA:
                return new FilaAAsterisco(clave, anterior, paradaFinal, tipoRuta);
            case MAS_LARGA:
                return new FilaAAsterisco(clave, anterior, paradaFinal, tipoRuta) {
                    @Override
                    public double getF() {
                        return -super.getF();
                    }
                };
            case MENOS_TIEMPO:
                return new FilaAAsterisco(clave, anterior, paradaFinal, tipoRuta) {
                    @Override
                    public double getF() {
                        int trasbordos = 0;
                        if (anterior != null) {
                            Parada anteriorP = anterior.getClave();
                            trasbordos += (int) anterior.getF();
                            FilaAAsterisco anteriorDeAnterior = anterior.anterior;
                            if (anteriorDeAnterior != null) {
                                Set<Linea> l1 = BusquedaRutaConLinea.lineasAlcanzanSiguienteParada(anteriorDeAnterior.getClave(), anteriorP);
                                Set<Linea> l2 = BusquedaRutaConLinea.lineasAlcanzanSiguienteParada(anteriorP, getClave());
                                l1.retainAll(l2);
                                int lineasComunes = l1.size();
                                if (lineasComunes <= 0) {
                                    trasbordos++;//abrantes: ruben darío
                                }
                            }
                        }
                        //Un trasbordo son 500 metros
                        return trasbordos * 500 + super.getF();
                    }
                };
            default:
                return new FilaAAsterisco(clave, anterior, paradaFinal, BusquedaRuta.DEFAULT_TIPO_RUTA);
        }
    }
}
