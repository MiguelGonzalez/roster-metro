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
import org.xml.sax.SAXException;
import rostermetro.domain.PlanoMetro;
import rostermetro.domain.PlanoMetroBuilder;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class ParserXML {

    public static PlanoMetro parse(Reader reader) throws IOException, SAXException {
        Folder planoMetroKML = new KMLParser().parse(reader).getFolder();
        PlanoMetroBuilder pmB = new PlanoMetroBuilder();

        for (Folder lineaKML : planoMetroKML.getFolders()) {
            pmB.registrarLinea(lineaKML);
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                pmB.put(paradaKML, lineaKML);
            }
        }
        return pmB.createPlanoMetro(planoMetroKML.getName());
    }

    public static PlanoMetro parse(InputStream resourceAsStream) throws IOException, SAXException {
        return parse(new InputStreamReader(resourceAsStream));
    }

    public static PlanoMetro parse(File file) throws IOException, SAXException {
        return parse(new FileReader(file));
    }
}
