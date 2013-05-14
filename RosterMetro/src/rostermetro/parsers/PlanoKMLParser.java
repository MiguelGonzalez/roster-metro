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
import rostermetro.planoBuilders.PlanoKMLBuilder;

/**
 * A partir de un fichero KML y usando la librería gekmlib se construye un
 * objeto Plano con toda la información. https://code.google.com/p/gekmllib/
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoKMLParser extends PlanoParser<PlanoKMLBuilder> {

    /**
     * Se inicializa usando un PlanoKMLBuilder
     */
    public PlanoKMLParser() {
        super(new PlanoKMLBuilder());
    }

    @Override
    public Plano parse(Reader reader) throws IOException, SAXException {
        Kml parsedKML = new KMLParser().parse(reader);

        Folder[] lineasKML;
        String tituloPlano;
        //Obtenemos el título del plano. (Puede estar en muchos sitios)
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

        //Vamos añadiendo líneas y sus paradas
        for (Folder lineaKML : lineasKML) {
            for (Placemark paradaKML : lineaKML.getPlacemarks()) {
                planoMetroBuilder.put(paradaKML, lineaKML);
            }
        }
        Plano plano = planoMetroBuilder.createPlano(tituloPlano);
        //La información que quedara en planoMetroBuilder no nos interesa más para próximos usos
        planoMetroBuilder.flush();
        return plano;
    }
}
