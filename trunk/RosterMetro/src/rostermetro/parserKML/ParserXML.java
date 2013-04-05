/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.parserKML;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Placemark;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import org.xml.sax.SAXException;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Linea;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class ParserXML {

    public static PlanoMetro parse(Reader reader) throws IOException, SAXException {
        Folder planoMetroKML = new KMLParser().parse(reader).getFolder();
        List<Linea> lineasMetro = new ArrayList<>();
        Set<Parada> paradasMetro = new HashSet<>();
        for (Folder lineaKML : planoMetroKML.getFolders()) {
            List<Parada> paradasLinea = new ArrayList<>();
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                Parada parada = new Parada(paradaKML.getName(), getCoordenada(paradaKML));
                paradasLinea.add(parada);
                paradasMetro.add(parada);
            }
            lineasMetro.add(new Linea(lineaKML.getName(), paradasLinea));
        }

        return new PlanoMetro(planoMetroKML.getName(), lineasMetro, paradasMetro);
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
