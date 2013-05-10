package rostermetroswing.components;

import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import rostermetro.busqueda.Ruta;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;

/**
 * Objeto que recibe una ruta y lo representa en un mapa de Google Maps (necesaria
 * conexión a Internet).
 * Además, por eficiencia, implementa una caché de últimos mapas buscados.
 * 
 * @author Jaime Bárez y Miguel González
 */
public class PlanoGoogleMaps extends JLabel {

    private ThreadObtieneImagenMapa obtenedor;
    private HashMap<String, Image> cache = new HashMap<>();
    public static final int W_GOOGLE = 640;
    public static final int H_GOOGLE = 480;
    
    private static final int MAX_CACHE_MAPAS = 50;

    public PlanoGoogleMaps() {
        setPreferredSize(new Dimension(W_GOOGLE, H_GOOGLE));
        obtenedor = new ThreadObtieneImagenMapa(this);
        obtenedor.start();
        obtenedor.yield();
    }

    public void pintarRuta(Ruta ruta) {
        setImagen(UIManager.getIcon("OptionPane.questionIcon"));
        if (ruta != null) {
            try {
                obtenedor.setUrl(new URL(getURL(ruta)));
            } catch (MalformedURLException | NullPointerException ex) {
                Logger.getLogger(PlanoGoogleMaps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    synchronized Image getCached(String urlString) {
        return cache.get(urlString);
    }

    synchronized void putInCache(String urlString, Image img) {
        cache.put(urlString, img);
        if (cache.size() > MAX_CACHE_MAPAS) {
            cache.entrySet().iterator().next();
            cache.entrySet().iterator().remove();
        }
    }

    private static String getURL(Ruta ruta) {
        StringBuilder url = new StringBuilder();
        List<Parada> listadoParadas = ruta.getListadoParadas();
        Coordenada cInicial = listadoParadas.get(0).getCoordenada();
        Coordenada cFinal = listadoParadas.get(listadoParadas.size() - 1).getCoordenada();
        url
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
            url.append("|").append(c.getLatitud()).append(",").append(c.getLongitud());
        }
        return url.toString();
    }

    void setImagen(Icon leida) {
        if (leida == null) {
            leida = UIManager.getIcon("OptionPane.errorIcon");
        }
        final Icon icono = leida;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setIcon(icono);
            }
        });
    }
}
