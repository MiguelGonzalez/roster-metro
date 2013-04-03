/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.parserKML;

import com.keithpower.gekmlib.KMLParser;
import com.keithpower.gekmlib.Kml;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import rostermetro.domain.PlanoMetro;

/**
 *
 * @author Ceura
 */
public class ParserXML {
    public static PlanoMetro parse(File fileXML) throws IOException, SAXException {
        Kml kmlRoot = new KMLParser().parse(fileXML); 
        
        return null;
        
    }
}
