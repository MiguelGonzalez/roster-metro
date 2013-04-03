package rostermetro;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Kml;
import com.keithpower.gekmlib.Placemark;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

/**
 *
 * @author ceura
 */
public class RosterMetro {

    public static void main(String[] args) throws IOException, SAXException {
        String file = "C:\\Users\\paracaidista\\Documents\\NetBeansProjects\\roster-metro\\RosterMetro\\src\\rostermetro\\metroMadrid.kml";
        
        Kml kmlRoot = new KMLParser().parse(new File(file)); 
        
        Folder folderMetro = kmlRoot.getFolder();
        
        Folder[] folderLineas = folderMetro.getFolders();
        
        for(Folder folderLinea : folderLineas) {
            
            
            Placemark[] paradas = folderLinea.getPlacemarks();
            
            for(Placemark parada : paradas) {
                if(parada.getName().equals("Sol Metro")) {
                    System.out.println(folderLinea.getName() + ", " + parada.getPoint().getNumericalCoordinates()[0] + ", " +  parada.getPoint().getNumericalCoordinates()[1]);
                }
                //System.out.println("  " + parada.getName());
                
            }
        }
        
        
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
