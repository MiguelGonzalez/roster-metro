package rostermetroswing.components;

import java.awt.Dimension;
import java.awt.Image;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import rostermetro.busqueda.*;
import rostermetro.domain.*;

/**
 * Objeto que recibe una ruta y lo representa en un mapa de Google Maps
 * (necesaria conexión a Internet). Usa un ThreadObtieneImagenMapa interno.
 *
 * @author Jaime Bárez y Miguel González
 */
public class PlanoGoogleMaps extends JLabel {

    public static final int W_GOOGLE = 640;
    public static final int H_GOOGLE = 480;
    public static final int MAX_CACHE_MAPAS = 100;
    private ThreadObtieneImagenMapa obtenedor;
    private final HashMap<String, Image> cache = new HashMap<>();

    /**
     * Inicializa el objeto
     */
    public PlanoGoogleMaps() {
        setSize(new Dimension(W_GOOGLE, H_GOOGLE));
        setSize(getPreferredSize());
        obtenedor = new ThreadObtieneImagenMapa(this);
        iniciarObtenedor();
    }

    /**
     * Avisa al thread que obtiene imagenes de que hay una nueva petición en
     * cola
     *
     * @param ruta
     */
    public void mostrarRuta(Ruta ruta) {
        setIcono(UIManager.getIcon("OptionPane.questionIcon"));
        if (ruta != null) {
            obtenedor.setUrl(createURL(ruta));
        }

    }

    /**
     * Dada una Srting que contiene una url, nos devuelve la imagen en caché(si
     * existe)
     *
     * @param urlString
     * @return
     */
    protected Image getCached(String urlString) {
        synchronized (cache) {
            return cache.get(urlString);
        }
    }

    /**
     * Pone en caché una imagen
     *
     * @param urlString
     * @param img
     */
    protected void putInCache(String urlString, Image img) {
        synchronized (cache) {
            cache.put(urlString, img);
            if (cache.size() > MAX_CACHE_MAPAS) {
                Iterator<Map.Entry<String, Image>> iterator = cache.entrySet().iterator();
                iterator.next();
                iterator.remove();
            }
        }
    }

    /**
     * Dada una ruta crea la URL para obtener la imagen de Google Maps de esa
     * ruta
     *
     * @param ruta
     * @return
     */
    private static URL createURL(Ruta ruta) {
        URL url = null;
        StringBuilder urlStrb = new StringBuilder();
        List<Parada> listadoParadas = ruta.getListadoParadas();
        Coordenada cInicial = listadoParadas.get(0).getCoordenada();
        Coordenada cFinal = listadoParadas.get(listadoParadas.size() - 1).getCoordenada();
        urlStrb
                .append("http://maps.google.com/maps/api/staticmap?")
                .append("&size=")
                .append(W_GOOGLE)
                .append("x")
                .append(H_GOOGLE)
                .append("&maptype=roadmap")
                .append("&markers=color:red|label:O|")
                .append(cInicial.getLatitud())
                .append(",")
                .append(cInicial.getLongitud())
                .append("&markers=color:green|label:D|")
                .append(cFinal.getLatitud())
                .append(",")
                .append(cFinal.getLongitud())
                .append("&sensor=false")
                .append("&path=color:0x0000ff|weight:5");
        for (Parada parada : listadoParadas) {
            Coordenada c = parada.getCoordenada();
            urlStrb.append("|").append(c.getLatitud()).append(",").append(c.getLongitud());
        }
        try {
            url = new URL(urlStrb.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PlanoGoogleMaps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    /**
     * Cambia el icono por el pasado a la función
     *
     * @param imagen
     */
    protected void setIcono(Icon imagen) {
        final String textToSet;
        if (imagen == null) {
            imagen = UIManager.getIcon("OptionPane.errorIcon");
            textToSet = ("<html><b>ERROR</b>: No hay conexión<br>o ha superado el límite de<br>peticiones a Google Maps</html>");
        } else {
            textToSet = null;
        }
        final Icon icono = imagen;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setText(textToSet);
                setIcon(icono);
            }
        });
    }

    /**
     * Inicializa el thread que obtiene imágenes de Google Maps
     */
    private void iniciarObtenedor() {
        obtenedor.start();
    }
}
