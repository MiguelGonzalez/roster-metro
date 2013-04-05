package rostermetro;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.SAXException;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;
import rostermetro.parserKML.ParserXML;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class RosterMetro {

    public static void main(String[] args) throws IOException, SAXException {

        PlanoMetro metroMadrid = ParserXML.parse(RosterMetro.class.getResourceAsStream("metroMadrid.kml"));
        //System.out.println(metroMadrid);
        System.out.println(Arrays.toString(metroMadrid.getParadas().toArray()));
        

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
