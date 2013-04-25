package rostermetro.builders;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.Placemark;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 * Crea un Plano a partir de datos KML de la biblioteca gekmllib
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoKMLBuilder extends AbstractPlanoBuilder<Placemark, Folder> {

    private static Coordenada getCoordenada(Placemark placeMark) {
        double[] numericalCoordinates = placeMark.getPoint().getNumericalCoordinates();
        Coordenada coordenada = new Coordenada(numericalCoordinates[0], numericalCoordinates[1]);
        return coordenada;
    }

    @Override
    protected Linea createLinea(Folder lineaGeneric) {
        return new Linea(lineaGeneric.getName());
    }

    @Override
    protected Parada createParada(Placemark paradaGeneric) {
        //System.out.println(paradaGeneric.getStyles()[0].getIconStyle().getColor());
        return new Parada(paradaGeneric.getName(), getCoordenada(paradaGeneric));
    }
}
