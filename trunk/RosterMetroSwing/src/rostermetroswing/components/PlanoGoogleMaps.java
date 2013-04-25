package rostermetroswing.components;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.OptionPaneUI;
import rostermetro.busqueda.commons.Ruta;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;
import rostermetro.busqueda.conLinea.ParadaRutaConLinea;

/**
 *
 * @author Ceura
 */
public class PlanoGoogleMaps extends JLabel {

    private ThreadPintado threadPintadoActual;
    private ThreadPintado nuevoThreadPintado;
    private static final int W_GOOGLE = 640;
    private static final int H_GOOGLE = 480;

    public PlanoGoogleMaps() {
        threadPintadoActual = new ThreadPintado(null);
        setPreferredSize(new Dimension(W_GOOGLE, H_GOOGLE));
    }

    public void pintarRuta(Ruta ruta) {
        nuevoThreadPintado = new ThreadPintado(ruta);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    threadPintadoActual.join();
                    threadPintadoActual = nuevoThreadPintado;
                    threadPintadoActual.start();
                } catch (Exception ex) {
                    //Logger.getLogger(PlanoGoogleMaps.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public void dibujarIcon(final Icon icon) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setIcon(icon);
            }
        });
    }

    class ThreadPintado extends Thread {

        private boolean pintando = true;
        private Ruta ruta;

        public ThreadPintado(Ruta ruta) {
            this.ruta = ruta;
        }

        public void parar() {
            setPintando(false);
        }

        @Override
        public void run() {
            if (ruta != null) {
                List<Parada> listadoParadas = ruta.getListadoParadas();

                if (listadoParadas == null) {
                    parar();
                }

                Parada pInicial = null;
                Parada pFinal = null;
                if (isPintando()) {

                    if (listadoParadas.size() > 0) {
                        pInicial = listadoParadas.get(0);
                    }
                    if (listadoParadas.size() > 1) {
                        pFinal = listadoParadas.get(listadoParadas.size() - 1);
                    } else {
                        pFinal = pInicial;
                    }

                    if (pFinal == null || pInicial == null) {
                        parar();
                    }
                }

                if (isPintando()) {

                    Image imagenPlano = getMap(pInicial, pFinal);

                    //Hack, algunas veces no lee a la primera vez
                    if (imagenPlano == null) {
                        imagenPlano = getMap(pInicial, pFinal);
                    }
                    if (isPintando()) {
                        if (imagenPlano == null) {
                            ImageIcon ic;
                            ic = new ImageIcon();
                            dibujarIcon(UIManager.getIcon("OptionPane.errorIcon"));
                        } else {
                            dibujarIcon(new ImageIcon(imagenPlano));
                        }
                    }
                }
            } else {
                dibujarIcon(UIManager.getIcon("OptionPane.questionIcon"));
            }


        }
        public static final int ZOOM = 12;
        private HashMap<String, Image> cache = new HashMap<>();

        private Image getMap(double latorigen, double lonOrigen, double latDestino, double lonDestino, int width, int height) {
            Image img = null;
            String url;
            try {
                url = "http://maps.google.com/maps/api/staticmap";
                url += "?"/*+"zoom=" + ZOOM */ + "&size=" + width + "x" + height;
                url += "&maptype=roadmap";
                url += "&markers=color:red|label:Origen|" + latorigen + "," + lonOrigen;
                url += "&markers=color:red|label:Destino|" + latDestino + "," + lonDestino;
                url += "&sensor=false";
                Image get = cache.get(url);
                if (get != null) {
                    img = get;
                } else {
                    img = ImageIO.read(new URL(
                            url));
                    cache.put(url, img);
                }
            } catch (Exception ex) {
                System.out.println("Error!" + ex);
                //@ToDo: devolver imagen error de red
            }
            return img;
        }

        private Image getMap(Parada pInicial, Parada pFinal) {
            Coordenada cInicial = pInicial.getCoordenada();
            Coordenada cfiNal = pFinal.getCoordenada();
            return getMap(cInicial.getLatitud(), cInicial.getLongitud(), cfiNal.getLatitud(), cfiNal.getLongitud(), W_GOOGLE, H_GOOGLE);
        }
        final Object sinc = new Object();

        /**
         * @return the pintando
         */
        public boolean isPintando() {
            synchronized (sinc) {
                return pintando;
            }

        }

        /**
         * @param pintando the pintando to set
         */
        public void setPintando(boolean pintando_) {
            synchronized (sinc) {
                this.pintando = pintando_;
            }
        }
    }
}
