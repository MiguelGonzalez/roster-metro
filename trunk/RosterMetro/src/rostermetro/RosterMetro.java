package rostermetro;

import java.io.IOException;
import java.util.Arrays;
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
        double distanceTo = metroMadrid.getParada("Moncloa Metro").getCoordenada().getDistanceTo(metroMadrid.getParada("Argüelles Metro").getCoordenada());
        System.out.println("distancia " + distanceTo);
        //        System.out.println("MetroMadrid:");
//        System.out.println(metroMadrid);
//        System.out.println("=-------=============");
//
//        System.out.println("Paradas:");
//        System.out.println(Arrays.toString(metroMadrid.getParadas().toArray()));
//        System.out.println("==========================");

//        System.out.println("Correspondencias:");
//        for (Linea linea : metroMadrid.getLineas()) {
//            for (Parada parada : linea.getParadas()) {
//                System.out.println(parada.getNombre());
//                System.out.println("Corresp:");
//                for (Linea lineaC : parada.getCorrespondencias()) {
//                    System.out.println(lineaC.getNombre());
//                }
//                System.out.println("-------------");
//            }
//        }

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
