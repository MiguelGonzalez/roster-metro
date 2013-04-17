/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.swing;

import com.sun.org.apache.bcel.internal.generic.FLOAD;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;
import rostermetro.domain.Ruta;

/**
 *
 * @author ceura
 */
public class BuscaParadasSimple extends JFrame {

    private final PlanoMetro planoMetro;
    private JComboBox origenComboBox;
    private JComboBox destinoComboBox;
    private JTextArea muestraRutasArea;
    private JTable tableMuestraRutas;
    private JLabel jLabel;

    public BuscaParadasSimple(PlanoMetro planoMetro) {
        this.planoMetro = planoMetro;

        setTitle("RutasMetro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(createPanelPrincipal());
        mostrarRuta();
        pack();
    }

    private JPanel createPanelPrincipal() {
        JPanel principal = new JPanel(new BorderLayout());

        JPanel derecha = new JPanel(new BorderLayout());
        Object[] paradas = planoMetro.getParadas().toArray();
        Arrays.sort(paradas, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Parada p1 = (Parada) o1;
                Parada p2 = (Parada) o2;
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            }
        });
        this.origenComboBox = new JComboBox(new DefaultComboBoxModel(paradas));
        this.destinoComboBox = new JComboBox(new DefaultComboBoxModel(paradas));
        JPanel derechaNoth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        derechaNoth.add(origenComboBox);
        derechaNoth.add(destinoComboBox);
        derecha.add(derechaNoth, BorderLayout.NORTH);
        //ImageIcon metroIcon = new ImageIcon(BuscaParadasSimple.class.getResource("metroMadrid.png"));
        //Image map = getMap(40.46667871671136, -3.689313162583721,0,0, 640, 480);
        this.jLabel = new JLabel();
        //jLabel.setPreferredSize(new Dimension(500, 500));
        derecha.add(jLabel, BorderLayout.SOUTH);
        origenComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRuta();
            }
        });
        destinoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRuta();
            }
        });
        this.muestraRutasArea = new JTextArea();
        muestraRutasArea.setEditable(false);
        muestraRutasArea.setRows(30);
        principal.add(derecha, BorderLayout.EAST);
        //principal.add(new JScrollPane(muestraRutasArea), BorderLayout.CENTER);
        tableMuestraRutas = new JTable();
        principal.add(new JScrollPane(tableMuestraRutas), BorderLayout.CENTER);
        return principal;
    }

    private void mostrarRuta() {
         //Image map = getMap(40.46667871671136, -3.689313162583721,0,0, 640, 480);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Parada pInicial = (Parada) origenComboBox.getSelectedItem();
                Parada pFinal = (Parada) destinoComboBox.getSelectedItem();
                final Ruta ruta = planoMetro.getRuta(pInicial, pFinal);

                muestraRutasArea.setText(ruta.toString());
                Object[][] objects = new Object[1][];
                objects[0] = ruta.getListadoParadas().toArray();
                TableModel tm = new AbstractTableModel() {
                    @Override
                    public int getRowCount() {
                        return ruta.getListadoParadas().size();
                    }

                    @Override
                    public int getColumnCount() {
                        return 1;
                    }

                    @Override
                    public Object getValueAt(int rowIndex, int columnIndex) {
                        return ruta.getListadoParadas().get(rowIndex);
                    }

                    @Override
                    public String getColumnName(int column) {

                        return "Ruta";
                    }
                };
                tableMuestraRutas.setModel(tm);
               
                
                jLabel.setIcon(new ImageIcon(getMap(pInicial,pFinal, 640, 480)));
            }
        });
    }

    public static final int ZOOM = 12;
    public Image getMap(double latorigen, double lonOrigen, double latDestino, double lonDestino,  int width, int height) {
        Image img = null;
        try {
            String url = "http://maps.google.com/maps/api/staticmap";
        url += "?zoom="+ZOOM+"&size=" + width + "x" + height;
        url += "&maptype=roadmap";
        url += "&markers=color:red|label:Otigen|" + latorigen + "," + lonOrigen;
        url += "&markers=color:red|label:Destino|" + latDestino + "," + lonDestino;
        url += "&sensor=false";
            img = ImageIO.read(new URL(
           url));

            //File outputfile = new File("map.png");
            //ImageIO.write(img, "png", outputfile);
            System.out.println("Saved!");
        } catch (Exception ex) {
            System.out.println("Error!" + ex);
        }
        return img;
    }
    
    public Image getMap(Parada pInicial, Parada pFinal, int width, int height) {
        Coordenada cInicial = pInicial.getCoordenada();
        Coordenada cfiNal = pFinal.getCoordenada();
        return getMap(cInicial.getLatitude(), cInicial.getLongitude(), cfiNal.getLatitude(), cfiNal.getLongitude(), width, height);
    }
}
