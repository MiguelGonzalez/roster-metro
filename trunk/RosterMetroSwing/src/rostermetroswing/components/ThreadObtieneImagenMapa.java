package rostermetroswing.components;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Thread para obtener de forma asíncrona una imagen del mapa a través del
 * servicio de Google Maps sin congelar la interfaz de usuario mientras se
 * descarga la imagen.
 *
 * Además, solo descarga la última imagen pedida, no descargando imagenes
 * pendientes en el caso de que se pidan muchas rutas seguidas y ya no se estén
 * visualizando. Así se evita perder tiempo, recursos y que el servidor de
 * Google Maps banee las conexiones temporalmente por hacer peticiones
 * desmesuradas.
 *
 * @author Jaime Bárez y Miguel González
 */
public class ThreadObtieneImagenMapa extends Thread {

    private final PlanoGoogleMaps planoGoogleMaps;
    private URL url;
    private boolean hayImagenesEnCola = false;

    public ThreadObtieneImagenMapa(PlanoGoogleMaps planoGoogleMaps) {
        this.planoGoogleMaps = planoGoogleMaps;
    }

    /**
     * Descarga una imagen mientras haya imágenes que descargar. Si no, duerme.
     */
    @Override
    public void run() {
        while (true) {
            URL myUrl = removeURL();
            if (myUrl != null) {
                String urlString = myUrl.toString();
                ImageIcon imageIcon = null;
                try {
                    //Intentamos obtener de caché
                    Image image = planoGoogleMaps.getCached(urlString);
                    if (image == null) {
                        image = ImageIO.read(myUrl);
                        planoGoogleMaps.putInCache(urlString, image);
                    }
                    imageIcon = new ImageIcon(image);
                } catch (IOException ex) {
                }
                planoGoogleMaps.setIcono(imageIcon);
            }
            dormir();
        }
    }

    /**
     * Avisa al thread de que descargue la imagen de la url cuando hay terminado
     * con la anterior
     *
     * @param url
     */
    public synchronized void setUrl(URL url) {
        this.url = url;
        avisarURLEnCola();
    }

    /**
     * Duerme si no hay imágenes en cola. Si no, no duerme.
     */
    private synchronized void dormir() {
        try {
            if (hayImagenesEnCola == true) {
                hayImagenesEnCola = false;
            } else {
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadObtieneImagenMapa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Usado por el método run(). Vacía la url a tratar y la devuelve.
     *
     * @return
     */
    private synchronized URL removeURL() {
        URL get = url;
        url = null;
        return get;
    }

    /**
     * Avisa al thread de que hay imágenes en cola. Si está dormido, lo
     * despierta
     */
    private synchronized void avisarURLEnCola() {
        hayImagenesEnCola = true;
        this.notify();
    }
}
