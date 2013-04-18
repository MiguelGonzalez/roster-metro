package rostermetro.parsers;

import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Placemark;
import java.io.IOException;
import java.io.Reader;
import org.xml.sax.SAXException;
import rostermetro.domain.PlanoMetro;
import rostermetro.builders.AbstractPlanoMetroBuilder;
import rostermetro.builders.PlanoMetroKMLBuilder;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoMetroKMLParser extends PlanoMetroParser {

    @Override
    public PlanoMetro parse(Reader reader) throws IOException, SAXException {
        Folder planoMetroKML = new KMLParser().parse(reader).getFolder();
        AbstractPlanoMetroBuilder pmB = new PlanoMetroKMLBuilder();

        for (Folder lineaKML : planoMetroKML.getFolders()) {
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                pmB.put(paradaKML, lineaKML);
            }
        }
        return pmB.createPlanoMetro(planoMetroKML.getName());
    }
}
