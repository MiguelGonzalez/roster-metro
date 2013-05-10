package rostermetro.parsers;

import com.keithpower.gekmlib.Document;
import com.keithpower.gekmlib.Folder;
import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Kml;
import com.keithpower.gekmlib.Placemark;
import java.io.IOException;
import java.io.Reader;
import org.xml.sax.SAXException;
import rostermetro.domain.Plano;
import rostermetro.planoBuilders.AbstractPlanoBuilder;
import rostermetro.planoBuilders.PlanoKMLBuilder;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoKMLParser extends PlanoParser {

    public PlanoKMLParser() {
        super(new PlanoKMLBuilder());
    }

    @Override
    public Plano parse(Reader reader) throws IOException, SAXException {

        Kml parsedKML = new KMLParser().parse(reader);
        AbstractPlanoBuilder planoMetroBuilder = getPlanoMetroBuilder();
        Folder[] lineasKML;
        String tituloPlano;

        Folder folder = parsedKML.getFolder();
        if (folder != null) {
            tituloPlano = folder.getName();
            lineasKML = folder.getFolders();

        } else {
            Document document = parsedKML.getDocument();
            tituloPlano = document.getName();
            lineasKML = document.getFolders();
            boolean recorrer = true;
            while (recorrer && lineasKML.length > 0) {
                Folder hijo = lineasKML[0];
                Folder[] hijoFolders = hijo.getFolders();
                if (hijoFolders.length > 0) {
                    lineasKML = hijoFolders;
                    tituloPlano = hijo.getName();
                } else {
                    recorrer = false;
                }
            }
        }


        for (Folder lineaKML : lineasKML) {
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                planoMetroBuilder.put(paradaKML, lineaKML);
            }
        }


        return planoMetroBuilder.createPlano(tituloPlano);
    }
}
