package rostermetro.parsers;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import rostermetro.Utilidades;
import rostermetro.domain.Plano;

/**
 *
 * @author ceura
 */
public class PlanoKMLParserTest {

    private static PlanoKMLParser planoMetroKmlParser;

    public PlanoKMLParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        planoMetroKmlParser = new PlanoKMLParser();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class PlanoMetroKMLParser.
     */
    @Test
    public void testParse() {
//        try {
//            System.out.println("parse");
//            Plano result = planoMetroKmlParser.parse(Utilidades.getPlanoAsStream(Utilidades.PlanoAlmacenado.CERCANIAS_MADRID_KML));
//            assertNotNull(result);
//            System.out.println(result);
//        } catch (IOException | SAXException ex) {
//            fail("Excepción ");
//        }
    }
}
