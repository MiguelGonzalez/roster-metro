package rostermetro.utilidades;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Usada para cargar los planos alojados en el paquete.
 *
 * @author Jaime Bárez y Miguel González
 */
public class Utilidades {

    public static enum PlanoAlmacenado {

        METRO("MadridMetro.kml"), RENFE("MadridCercanias.kml");
        private final String nombrePlano;

        private PlanoAlmacenado(String nombrePlano) {
            this.nombrePlano = nombrePlano;
        }

        @Override
        public String toString() {
            return nombrePlano;
        }
    }
    private static final List<PlanoAlmacenado> planosAlmacenados = Arrays.asList(PlanoAlmacenado.values());

    public static InputStream getPlanoAsStream(PlanoAlmacenado plano) {
        return Utilidades.class.getResourceAsStream(plano.toString());
    }

    public static PlanoAlmacenado[] getPlanosAlmacenados() {
        PlanoAlmacenado[] planos = new PlanoAlmacenado[planosAlmacenados.size()];
        planos = planosAlmacenados.toArray(planos);
        return planos;
    }
}
