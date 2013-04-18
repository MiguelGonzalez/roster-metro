package rostermetro;

import java.io.InputStream;

/**
 * Usada para cargar los ficheros alojados en el paquete
 *
 * @author Jaime Bárez y Miguel González
 */
public class Utilidades {
    
    public static enum Plano {
        
        METRO_MADRID_KML("metroMadrid.kml"),
        //METRO_EJEMPLO("rutaEjemplo.kml"),
        ;
        private String plano;
        
        Plano(String plano) {
            this.plano = plano;
        }
        
        @Override
        public String toString() {
            return plano.toString();
        }
    };
    
    public static InputStream getPlanoAsStream(Plano plano) {
        return Utilidades.class.getResourceAsStream(plano.toString());
    }
}
