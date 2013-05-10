/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda;

import java.util.Set;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * @author ceura
 */
class FilaAAsteriscoMenosTrasbordos extends FilaAAsterisco {

    public FilaAAsteriscoMenosTrasbordos(Parada clave, FilaAAsterisco anterior, Parada paradaFinal, BusquedaRuta.TipoRuta tipoRuta) {
        super(clave, anterior, paradaFinal, tipoRuta);
    }

    @Override
    public double getF() {
        int trasbordos = getTrasbordos();
        
        return trasbordos*100000000000000d + (super.getF());
    }

    private int getTrasbordos() {
        int trasbordos = 0;
        if (getAnterior() != null) {
            Parada anteriorP = getAnterior().getClave();
            trasbordos += (int) ((FilaAAsteriscoMenosTrasbordos)getAnterior()).getTrasbordos();
            FilaAAsterisco anteriorDeAnterior = getAnterior().getAnterior();
            if (anteriorDeAnterior != null) {
                Set<Linea> l1 = anteriorDeAnterior.getClave().getLineasComunes(anteriorP);
                Set<Linea> l2 = anteriorP.getLineasComunes(getClave());
                l1.retainAll(l2);
                int lineasComunes = l1.size();
                if (lineasComunes <= 0) {
                    trasbordos++;//abrantes: ruben darío
                }
            }
        }
        return trasbordos;
    }
}
