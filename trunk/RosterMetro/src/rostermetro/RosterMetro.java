package rostermetro;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Kml;
import com.keithpower.gekmlib.Placemark;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import rostermetro.domain.PlanoMetro;
import rostermetro.parserKML.ParserXML;

/**
 *
 * @author ceura
 */
public class RosterMetro {

    public static void main(String[] args) throws IOException, SAXException {

        PlanoMetro metroMadrid = ParserXML.parse(RosterMetro.class.getResourceAsStream("metroMadrid.kml"));
        System.out.println(metroMadrid);

        /*
         * Procesador fichero KML y Líneas Metro
         */

        /*
         PlanoDeMetro
         * Nombre
         * Listado de lineas
         * ObtenerLíneas(Dada parada)
         Lineas
         * Nombre
         * Listado de paradas
         Parada
         * Nombre
         * Coordenadas
         */

        /*
         * Algoritmo de búsqueda
         * Parada inicial
         * Parada final
         * Arcos lo forman las paradas de las líneas
         */

        /*
         * Información a parsear (quitando datos que no vamos a leer)
         * 
         * <Folder> <- Agrupación de paradas de metro
         <name>Madrid Metro Line 1</name>
         <Placemark> <- Parada
         <name>Plaza de Castilla Metro</name>
         <description>Lines 1, 9, 10. Bus terminal</description>
         <Point id="khPoint540">
         <coordinates>-3.689170797285812,40.46674869438549,0</coordinates>
         </Point>
         </Placemark>
         <Placemark>
         <name>Valdeacederas Metro</name>
         <description>Line 1</description>
         <Point>
         <coordinates>-3.694774074060931,40.46447773263045,0</coordinates>
         </Point>
         </Placemark>
         */
    }
}
