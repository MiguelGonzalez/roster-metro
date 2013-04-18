/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.parsers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.xml.sax.SAXException;
import rostermetro.domain.PlanoMetro;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class PlanoMetroParser {

    public abstract PlanoMetro parse(Reader reader) throws IOException, SAXException;

    public final PlanoMetro parse(InputStream resourceAsStream) throws IOException, SAXException {
        return parse(new InputStreamReader(resourceAsStream));
    }

    public final PlanoMetro parse(File file) throws IOException, SAXException {
        return parse(new FileReader(file));
    }
}
