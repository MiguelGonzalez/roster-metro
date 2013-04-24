package rostermetroswing.components;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import rostermetro.busqueda.RutaConLinea;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;
import rostermetro.domain.ParadaRuta;

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
    
    public void pintarRuta(RutaConLinea ruta) {
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
    
    public void dibujarImageIcon(final Image imagenIcon) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (imagenIcon != null) {
                    setIcon(new ImageIcon(imagenIcon));
                } else {
                    //setIcon(Imagen ruta inexistente); 
                    //ToDo: @miguel icono ruta inexistente
                }
            }
        });
    }
    
    class ThreadPintado extends Thread {
        
        private boolean pintando = true;
        private RutaConLinea ruta;
        
        public ThreadPintado(RutaConLinea ruta) {
            this.ruta = ruta;
        }
        
        public void parar() {
            setPintando(false);
        }
        
        @Override
        public void run() {
            if (ruta != null) {
                List<ParadaRuta> listadoParadas = ruta.getParadasRuta();

                if(listadoParadas == null) {
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
                    
                    Image imagenPlano = getMap(pInicial, pFinal, W_GOOGLE, H_GOOGLE);

                    //Hack, algunas veces no lee a la primera vez
                    if (imagenPlano == null) {
                        imagenPlano = getMap(pInicial, pFinal, W_GOOGLE, H_GOOGLE);
                    }
                    if (isPintando()) {
                        if(imagenPlano==null){
                            //ToDo: dibujarImageIcon(Imagen error de red);
                        }else{
                            dibujarImageIcon(imagenPlano);
                        }
                    }
                }
            } else {
                dibujarImageIcon(null);
            }
            
            
        }
        public static final int ZOOM = 12;
        
        private Image getMap(double latorigen, double lonOrigen, double latDestino, double lonDestino, int width, int height) {
            Image img = null;
            try {
                String url = "http://maps.google.com/maps/api/staticmap";
                url += "?zoom=" + ZOOM + "&size=" + width + "x" + height;
                url += "&maptype=roadmap";
                url += "&markers=color:red|label:Otigen|" + latorigen + "," + lonOrigen;
                url += "&markers=color:red|label:Destino|" + latDestino + "," + lonDestino;
                url += "&sensor=false";
                img = ImageIO.read(new URL(
                        url));
            } catch (Exception ex) {
                System.out.println("Error!" + ex);
                //@ToDo: devolver imagen error de red
            }
            return img;
        }
        
        private Image getMap(Parada pInicial, Parada pFinal, int width, int height) {
            Coordenada cInicial = pInicial.getCoordenada();
            Coordenada cfiNal = pFinal.getCoordenada();
            return getMap(cInicial.getLatitud(), cInicial.getLongitud(), cfiNal.getLatitud(), cfiNal.getLongitud(), width, height);
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
