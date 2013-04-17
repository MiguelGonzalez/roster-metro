/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetroswing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.xml.sax.SAXException;
import rostermetro.RosterMetro;
import rostermetro.domain.Coordenada;
import rostermetro.domain.Parada;
import rostermetro.domain.PlanoMetro;
import rostermetro.domain.Ruta;
import rostermetro.parserKML.ParserXML;
import rostermetroswing.components.PlanoMetroDibujo;
import rostermetroswing.model.TablaRutaModel;

/**
 *
 * @author paracaidista
 */
public class RosterMetroSwing extends JFrame {

    private PlanoMetro planoMetro;
    private JComboBox origenComboBox;
    private JComboBox destinoComboBox;
    private JTable tableMuestraRutas;
    private PlanoMetroDibujo planoMetroDibujo;
    private TablaRutaModel tablaRutaModel;
    private Parada[] paradas;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RosterMetroSwing buscaParadasSimple =
                        new RosterMetroSwing("Roster metro");
                buscaParadasSimple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                buscaParadasSimple.setBounds(100, 100, 1000, 500);
                buscaParadasSimple.setVisible(true);
            }
        });
    }

    public RosterMetroSwing(String nombreAplicacion) {
        super(nombreAplicacion);

        try {
            planoMetro = ParserXML.parse(RosterMetro.class.
                    getResourceAsStream("metroMadrid.kml"));

            initComponenets();
            
            initListeners();
            
            initInterfaz();

            mostrarRuta();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error",
                    "No se pudo cargar el mapa de metro",
                    JOptionPane.ERROR_MESSAGE);

            Logger.getLogger(RosterMetroSwing.class.getName()).log(Level.SEVERE, null, ex);


        }
    }
    


    private void initComponenets() {
        Collection<Parada> coleccionParadas = planoMetro.getParadas();
        paradas = new Parada[coleccionParadas.size()];
        paradas = coleccionParadas.toArray(paradas);
        
        Arrays.sort(paradas, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Parada p1 = (Parada) o1;
                Parada p2 = (Parada) o2;
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            }
        });
        
        
        origenComboBox = new JComboBox(new DefaultComboBoxModel(paradas));
        destinoComboBox = new JComboBox(new DefaultComboBoxModel(paradas));
        
        tablaRutaModel = new TablaRutaModel();
        tableMuestraRutas = new JTable(tablaRutaModel);
        
        planoMetroDibujo = new PlanoMetroDibujo();
    }
    
    private void initListeners() {
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
    }
    
    private void initInterfaz() {
        Container container = getContentPane();
        
        JPanel panelIzquierda = new JPanel(new BorderLayout());
        panelIzquierda.add(new JScrollPane(tableMuestraRutas), BorderLayout.CENTER);
        container.add(panelIzquierda, BorderLayout.WEST);
        
        JPanel panelSuperiorCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperiorCentro.add(origenComboBox);
        panelSuperiorCentro.add(destinoComboBox);
        
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelSuperiorCentro, BorderLayout.NORTH);
        panelCentro.add(planoMetroDibujo, BorderLayout.CENTER);
        
        container.add(panelCentro, BorderLayout.CENTER);
    }

    private void mostrarRuta() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Parada pInicial = (Parada) origenComboBox.getSelectedItem();
                Parada pFinal = (Parada) destinoComboBox.getSelectedItem();
                final Ruta ruta = planoMetro.getRuta(pInicial, pFinal);
                
                tablaRutaModel.replaceRuta(ruta);
                planoMetroDibujo.pintarRuta(ruta);
            }
        });
    }
    
}
