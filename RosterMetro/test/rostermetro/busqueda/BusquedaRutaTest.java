/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.busqueda;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import rostermetro.Utilidades;
import rostermetro.busqueda.BusquedaRuta.TipoRuta;
import rostermetro.domain.Parada;
import rostermetro.domain.Plano;
import rostermetro.parsers.PlanoKMLParser;

/**
 *
 * @author ceura
 */
public class BusquedaRutaTest {
    
    public BusquedaRutaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of calcularRuta method, of class BusquedaRuta.
     */
    @Test
    public void testCalcularRuta_Parada_Parada()  {
        try {
            Plano result = new PlanoKMLParser().parse(Utilidades.getPlanoAsStream(Utilidades.Plano.METRO_MADRID_KML));
            Ruta calcularRuta = new BusquedaRuta().calcularRuta(result.getParada("Sol Metro"), result.getParada("Aluche Metro"));
            System.out.println("RUTA CALCULADA\n"+calcularRuta);
        } catch (IOException | SAXException ex) {
            fail("Error anterior en el parser");
        }
    }

    /**
     * Test of calcularRuta method, of class BusquedaRuta.
     */
    @Test
    public void testCalcularRuta_3args() {
        /*System.out.println("calcularRuta");
        Parada paradaInicio = null;
        Parada paradaFinal = null;
        TipoRuta tipoRuta = null;
        BusquedaRuta instance = new BusquedaRuta();
        Ruta expResult = null;
        Ruta result = instance.calcularRuta(paradaInicio, paradaFinal, tipoRuta);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }
}
