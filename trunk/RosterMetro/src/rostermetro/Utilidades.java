package rostermetro;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Usada para cargar los planos alojados en el paquete.
 *
 * @author Jaime Bárez y Miguel González
 */
public class Utilidades {

    public static class PlanoAlmacenado {

        public static final Set<PlanoAlmacenado> planosAlmacenados = new HashSet<>();

        static {
            planosAlmacenados.add(new PlanoAlmacenado("MadridMetro.kml"));
            planosAlmacenados.add(new PlanoAlmacenado("MadridCercanias.kml"));
        }
        private String ruta;

        private PlanoAlmacenado(String ruta) {
            this.ruta = ruta;
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
        PlanoAlmacenado[] array = new PlanoAlmacenado[PlanoAlmacenado.planosAlmacenados.size()];
        return PlanoAlmacenado.planosAlmacenados.toArray(array);
    }
}
