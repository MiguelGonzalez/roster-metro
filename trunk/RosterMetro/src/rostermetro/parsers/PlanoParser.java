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
 *
 * @author Jaime Bárez y Miguel González
 */
public abstract class PlanoParser {

    private final AbstractPlanoBuilder planoMetroBuilder;

    public PlanoParser(AbstractPlanoBuilder planoMetroBuilder) {
        this.planoMetroBuilder = planoMetroBuilder;
    }

    public abstract Plano parse(Reader reader) throws IOException, SAXException;

    public final Plano parse(InputStream resourceAsStream) throws IOException, SAXException {
        return parse(new InputStreamReader(resourceAsStream));
    }

    public final Plano parse(File file) throws IOException, SAXException {
        return parse(new FileReader(file));
    }

    /**
     * @return the planoMetroBuilder
     */
    public AbstractPlanoBuilder getPlanoMetroBuilder() {
        return planoMetroBuilder;
    }
}
