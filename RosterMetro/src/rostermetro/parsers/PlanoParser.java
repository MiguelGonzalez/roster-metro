package rostermetro.parsers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.xml.sax.SAXException;
import rostermetro.planoBuilders.AbstractPlanoBuilder;
import rostermetro.domain.Plano;

/**
 * Clase abstracta utilizada por las que hereden para construir el objeto Plano
 * a través de fuentes externas. Permitiría implementar parseadores de ficheros
 * KML, txt, excel, etc. Se vale de un AbstractPlanoBuilder para ir construyendo
 * las objetos de rostermetro.domain a través de clases que usa el parseador
 * para referirse a los objetos
 *
 * @author Jaime Bárez y Miguel González
 * @param <P> Tipo concreto de AbstractPlanoBuilder utilizado
 */
public abstract class PlanoParser<P extends AbstractPlanoBuilder> {

    protected final P planoMetroBuilder;

    /**
     * Inicializa el builder
     *
     * @param planoMetroBuilder
     */
    public PlanoParser(P planoMetroBuilder) {
        this.planoMetroBuilder = planoMetroBuilder;
    }

    /**
     * A implementar por los que hereden. Leerá datos y se valdrá del Builder
     * para convertir las clases externas a clases del dominio conocidas por el
     * programa
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public abstract Plano parse(Reader reader) throws IOException, SAXException;

    /**
     * Parseo con entrada de inputStream
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public final Plano parse(InputStream inputStream) throws IOException, SAXException {
        return parse(new InputStreamReader(inputStream));
    }

    /**
     * Parseo con entrada de fichero
     *
     * @param file
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public final Plano parse(File file) throws IOException, SAXException {
        return parse(new FileReader(file));
    }
}
