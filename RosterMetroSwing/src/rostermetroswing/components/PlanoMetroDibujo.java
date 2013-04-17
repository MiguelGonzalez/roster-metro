/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetroswing.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;
import rostermetro.domain.Ruta;

/**
 *
 * @author Ceura
 */
public class PlanoMetroDibujo extends JPanel {
    private ThreadPintado threadPintado;
    private Image imagenPlano;
    
    public PlanoMetroDibujo() {
        threadPintado = new ThreadPintado(null, null);
        imagenPlano = null;
    }
    
    
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics g = (Graphics) graphics.create();
        
        //Pintamos el fondo blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if(imagenPlano != null) {
            g.drawImage(imagenPlano, 0, 0, getWidth(), getHeight(), null);
        }
    }
    
    public void pintarRuta(Ruta ruta) {
        threadPintado.parar();
        threadPintado = new ThreadPintado(this, ruta);
        threadPintado.start();
    }
    
    public void dibujarImageIcon(Image imagenIcon) {
        this.imagenPlano = imagenIcon;
        
        repaint();
    }
    
    
}

class ThreadPintado extends Thread {

    private boolean pintando = true;
    private PlanoMetroDibujo planoMetroDibujo;
    private Ruta ruta;
    private Image imagenPlano;
    
    public ThreadPintado(PlanoMetroDibujo planoMetroDibujo, Ruta ruta) {
        this.planoMetroDibujo = planoMetroDibujo;
        this.ruta = ruta;
    }
    
    public void parar() {
        pintando = false;
    }
    
    @Override
    public void run() {
        List<Parada> listadoParadas = ruta.getListadoParadas();
        Parada pInicial = null;
        Parada pFinal = null;
        if(listadoParadas.size() > 0) {
            pInicial = listadoParadas.get(0);
        }
        if(listadoParadas.size() > 1) {
            pFinal = listadoParadas.get(listadoParadas.size() - 1);
        } else {
            pFinal = pInicial;
        }
        
        if(pFinal == null || pInicial == null) {
            parar();
        }
        
        if(pintando) {
            imagenPlano = getMap(pInicial,pFinal, 640, 480);

            //Hack, algunas veces no lee a la primera vez
            if(imagenPlano == null) {
                imagenPlano = getMap(pInicial,pFinal, 640, 480);
            }
        }
        
        if(pintando) {
            planoMetroDibujo.dibujarImageIcon(imagenPlano);
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
        }
        return img;
    }

    private Image getMap(Parada pInicial, Parada pFinal, int width, int height) {
        Coordenada cInicial = pInicial.getCoordenada();
        Coordenada cfiNal = pFinal.getCoordenada();
        return getMap(cInicial.getLatitude(), cInicial.getLongitude(), cfiNal.getLatitude(), cfiNal.getLongitude(), width, height);
    }
    
}