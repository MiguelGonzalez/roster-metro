package rostermetro.cargaMapas;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Usada para cargar los planos alojados en el paquete.
 *
 * @author Jaime Bárez y Miguel González
 */
public class CargaMapas {

    /**
     * Representa un plano almacenado
     */
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
    /**
     * Lista de todos los planos almacenados
     */
    private static final List<PlanoAlmacenado> planosAlmacenados = Arrays.asList(PlanoAlmacenado.values());

    /**
     * Devuelve un plano como inputStream
     *
     * @param plano
     * @return
     */
    public static InputStream getPlanoAsStream(PlanoAlmacenado plano) {
        return CargaMapas.class.getResourceAsStream(plano.toString());
    }

    /**
     * Devuelve un array con los planos almacenados
     *
     * @return
     */
    public static PlanoAlmacenado[] getPlanosAlmacenados() {
        PlanoAlmacenado[] planos = new PlanoAlmacenado[planosAlmacenados.size()];
        planos = planosAlmacenados.toArray(planos);
        return planos;
    }
}
