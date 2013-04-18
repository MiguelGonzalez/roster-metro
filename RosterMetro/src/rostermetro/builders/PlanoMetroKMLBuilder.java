package rostermetro.builders;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.Placemark;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoMetroKMLBuilder extends AbstractPlanoMetroBuilder<Placemark, Folder> {
    
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
        return new Parada(paradaGeneric.getName(), getCoordenada(paradaGeneric));
    }
}
