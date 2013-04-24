package rostermetroswing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.xml.sax.*;
import rostermetro.*;
import rostermetro.busqueda.*;
import rostermetro.domain.*;
import rostermetro.parsers.*;
import rostermetroswing.components.*;
import rostermetroswing.model.*;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class RosterMetroSwing extends JFrame {

    private Plano plano;
    private JComboBox<Parada> origenCBox;
    private JComboBox<Parada> destinoCBox;
    private JComboBox<Utilidades.PlanoAlmacenado> planoComboBox;
    private JTable rutaJTable;
    private PlanoGoogleMaps planoMetroDibujo;
    private PlanoParser planoParser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RosterMetroSwing buscaParadasSimple =
                        new RosterMetroSwing("Roster Metro");
                buscaParadasSimple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                buscaParadasSimple.pack();
                buscaParadasSimple.setLocationRelativeTo(null);

                buscaParadasSimple.setVisible(true);
            }
        });
    }

    public RosterMetroSwing(String nombreAplicacion) {
        super(nombreAplicacion);
        planoParser = new PlanoKMLParser();

        initComponenets();

        initListeners();

        initInterfaz();
        setPlano((Utilidades.PlanoAlmacenado) planoComboBox.getSelectedItem());
    }

    private void initComponenets() {

        planoComboBox = new JComboBox<>(new DefaultComboBoxModel<>(Utilidades.getPlanosAlmacenados()));
        origenCBox = new JComboBox<>();
        destinoCBox = new JComboBox<>();
        rutaJTable = new JTable();

        planoMetroDibujo = new PlanoGoogleMaps();
    }

    private void initListeners() {
        ActionListener muestraRuta = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRuta();
            }
        };
        planoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setPlano((Utilidades.PlanoAlmacenado) planoComboBox.getSelectedItem());

            }
        });
        origenCBox.addActionListener(muestraRuta);
        destinoCBox.addActionListener(muestraRuta);

    }

    private void initInterfaz() {
        Container container = getContentPane();
        JPanel principal = new JPanel(new BorderLayout());
        JPanel panelIzquierda = new JPanel(new BorderLayout());
        panelIzquierda.add(new JScrollPane(rutaJTable), BorderLayout.CENTER);
        principal.add(panelIzquierda, BorderLayout.WEST);

        JPanel panelSuperiorCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperiorCentro.add(origenCBox);
        panelSuperiorCentro.add(destinoCBox);
        panelSuperiorCentro.add(planoComboBox);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelSuperiorCentro, BorderLayout.NORTH);
        panelCentro.add(planoMetroDibujo, BorderLayout.CENTER);

        principal.add(panelCentro, BorderLayout.CENTER);
        container.add(principal);
    }

    private void mostrarRuta() {
        final Parada pInicial = (Parada) origenCBox.getSelectedItem();
        final Parada pFinal = (Parada) destinoCBox.getSelectedItem();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                RutaConLinea ruta = new BusquedaRuta().calcularRuta(pInicial, pFinal);
                if (ruta != null) {
                    rutaJTable.setModel(new TablaRutaModel(ruta));
                    planoMetroDibujo.pintarRuta(ruta);
                } else {
                    rutaJTable.setModel(new TablaRutaModel(null));
                    planoMetroDibujo.pintarRuta(null);
                }
            }
        });
    }

    private void setPlano(Utilidades.PlanoAlmacenado parse) {
        try {
            plano = planoParser.parse(Utilidades.getPlanoAsStream(parse));
        } catch (IOException | SAXException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error",
                    "No se pudo cargar el mapa de metro",
                    JOptionPane.ERROR_MESSAGE);

            Logger.getLogger(RosterMetroSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
        setModels();
        mostrarRuta();
        pack();
    }

    private void setModels() {
        Collection<Parada> coleccionParadas = plano.getParadas();
        Parada[] paradas = new Parada[coleccionParadas.size()];
        paradas = coleccionParadas.toArray(paradas);
        //Orden alfabético
        Arrays.sort(paradas, new Comparator<Parada>() {
            @Override
            public int compare(Parada o1, Parada o2) {
                return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });

        origenCBox.setModel(new DefaultComboBoxModel<>(paradas));
        destinoCBox.setModel(new DefaultComboBoxModel<>(paradas));


    }
}
