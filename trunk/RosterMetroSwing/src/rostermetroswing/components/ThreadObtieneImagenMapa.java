package rostermetroswing.components;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Thread para obtener de forma asíncrona una imagen del mapa a través
 * del servicio de Google Maps sin congelar la interfaz de usuario mientras se
 * descarga la imagen.
 * 
 * Además, solo descarga la última imagen pedida, no descargando imagenes pendientes
 * en el caso de que se pidan muchas rutas seguidas y ya no se estén visualizando.
 * 
 * @author Jaime Bárez y Miguel González
 */
public class ThreadObtieneImagenMapa extends Thread {

    private final PlanoGoogleMaps planoGoogleMaps;
    private URL url;

    public ThreadObtieneImagenMapa(PlanoGoogleMaps planoGoogleMaps) {
        this.planoGoogleMaps = planoGoogleMaps;
    }

    @Override
    public void run() {
        while (true) {
            URL myUrl = getUrl();
            if (myUrl != null) {
                ImageIcon leida = null;
                try {
                    leida = new ImageIcon(ImageIO.read(myUrl));
                } catch (IOException ex) {
                }
                planoGoogleMaps.setImagen(leida);
            }
            dormir();
        }
    }
    private boolean hayMas= false;
    public synchronized void setUrl(URL url) {
        this.url = url;
        setHaymas(true);
    }

    private synchronized void dormir() {
        try {
            if(hayMas==true){
                hayMas=false;
            } else{
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadObtieneImagenMapa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    private synchronized URL getUrl() {
        URL get = url;
        url=null;
        return get;
    }

    private synchronized void setHaymas(boolean b) {
        hayMas = b;
        this.notify();
    }
}
