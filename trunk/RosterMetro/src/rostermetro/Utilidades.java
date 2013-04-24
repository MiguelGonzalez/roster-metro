package rostermetro;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Usada para cargar los ficheros alojados en el paquete.
 *
 * @author Jaime Bárez y Miguel González
 */
public class Utilidades {

    private static List<PlanoAlmacenado> planosAlmacenados = new LinkedList<>();

    public static enum PlanoAlmacenado {

        METRO_MADRID_KML("MadridMetro.kml"),
        CERCANIAS_MADRID_KML("MadridCercanias.kml");
        private String ruta;

        PlanoAlmacenado(String ruta) {
            this.ruta = ruta;
            planosAlmacenados.add(this);
        }

        @Override
        public String toString() {
            return ruta;
        }
    };

    public static InputStream getPlanoAsStream(PlanoAlmacenado plano) {
        return Utilidades.class.getResourceAsStream(plano.toString());
    }

    public static PlanoAlmacenado[] getPlanosAlmacenados() {
        PlanoAlmacenado e = PlanoAlmacenado.CERCANIAS_MADRID_KML;
        PlanoAlmacenado[] array = new PlanoAlmacenado[planosAlmacenados.size()];
        return planosAlmacenados.toArray(array);
    }
}
