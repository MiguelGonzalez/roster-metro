/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.parserKML;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Kml;
import com.keithpower.gekmlib.Placemark;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;

/**
 *
 * @author Ceura
 */
public class ParserXML {

    public static PlanoMetro parse(Reader reader) throws IOException, SAXException {
        Kml kmlRoot = new KMLParser().parse(reader);
        Folder planoMetroKML = kmlRoot.getFolder();
        ArrayList<Linea> lineas = new ArrayList<>();
        for (Folder lineaKML : planoMetroKML.getFolders()) {
            ArrayList<Parada> paradas = new ArrayList<>();
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                paradas.add(new Parada(paradaKML.getName(), getCoordenada(paradaKML)));
            }
            lineas.add(new Linea(lineaKML.getName(), paradas));
        }

        return new PlanoMetro(planoMetroKML.getName(), lineas);
    }

    public static PlanoMetro parse(InputStream resourceAsStream) throws IOException, SAXException {
        return parse(new InputStreamReader(resourceAsStream));
    }

    public static PlanoMetro parse(File file) throws IOException, SAXException {
        return parse(new FileReader(file));
    }

    private static Coordenada getCoordenada(Placemark paradaKML) {
        double[] numericalCoordinates = paradaKML.getPoint().getNumericalCoordinates();
        Coordenada coordenada = new Coordenada(numericalCoordinates[0], numericalCoordinates[1]);

        return coordenada;
    }
}
