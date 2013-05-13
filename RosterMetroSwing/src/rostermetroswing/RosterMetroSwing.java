package rostermetroswing;

import rostermetro.utilidades.Utilidades;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.xml.sax.*;
import rostermetro.busqueda.*;
import rostermetro.busqueda.conLinea.*;
import rostermetro.domain.*;
import rostermetro.parsers.*;
import rostermetroswing.components.*;
import rostermetroswing.model.*;

/**
 * Aplicación SWING utlizando la librería RosterMetro para permitir al usuario
 * buscar una ruta entre dos parádas.
 * Se muestran los resultados en una tabla y en un mapa de google maps.
 * 
 * @author Jaime Bárez y Miguel González
 */
public class RosterMetroSwing extends JFrame {

    public static final String TITULO = "Roster Metro";
    public static final Dimension MINIMUM = new Dimension(100,100);
    private Plano plano;
    private JComboBox<Parada> origenCBox;
    private JComboBox<Parada> destinoCBox;
    private JComboBox<BusquedaRuta.TipoRuta> tiposRutasCBox;
    private JComboBox<Utilidades.PlanoAlmacenado> planoComboBox;
    private JTable rutaJTable;
    private PlanoGoogleMaps planoMetroDibujo;
    private PlanoParser planoParser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RosterMetroSwing buscaParadasSimple =
                        new RosterMetroSwing(TITULO);
                buscaParadasSimple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                buscaParadasSimple.setMinimumSize(MINIMUM);
                
                buscaParadasSimple.setLocationRelativeTo(null);
                
                buscaParadasSimple.setVisible(true);
                buscaParadasSimple.pack();
            }
        });
    }

    /**
     * Inicializa componentes, listeners y construye la interfaz
     *
     * @param nombreAplicacion
     */
    public RosterMetroSwing(String nombreAplicacion) {
        super(nombreAplicacion);
        planoParser = new PlanoKMLParser();

        initComponents();

        initListeners();

        initInterfaz();
        setPlano((Utilidades.PlanoAlmacenado) planoComboBox.getSelectedItem());
    }

    /**
     * Inicializa los jCombobox, la tabla y el plano de Google Maps
     */
    private void initComponents() {

        planoComboBox = new JComboBox<>(new DefaultComboBoxModel<>(Utilidades.getPlanosAlmacenados()));
        origenCBox = new JComboBox<>();
        destinoCBox = new JComboBox<>();
        tiposRutasCBox = new JComboBox<>(BusquedaRuta.TipoRuta.values());

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
        origenCBox.addActionListener(muestraRuta);
        destinoCBox.addActionListener(muestraRuta);
        tiposRutasCBox.addActionListener(muestraRuta);
        planoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setPlano((Utilidades.PlanoAlmacenado) planoComboBox.getSelectedItem());

            }
        });

    }
    /**
     * Monta todos los componentes en paneles de forma ordenada
     */
    private void initInterfaz() {
        
        Border b = new BevelBorder(BevelBorder.LOWERED);
        Border b2 = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(b2, b);
        
        Container container = getContentPane();
        JPanel principal = new JPanel(new BorderLayout());

        JPanel panelIzquierda = new JPanel(new BorderLayout());
        panelIzquierda.add(new JScrollPane(rutaJTable), BorderLayout.CENTER);
        panelIzquierda.add(tiposRutasCBox, BorderLayout.NORTH);
        
        panelIzquierda.setBorder(compoundBorder);

        principal.add(panelIzquierda, BorderLayout.WEST);

        JPanel panelSuperiorCentro = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10,10,5,20);  //right padding
        panelSuperiorCentro.add(new JLabel("Origen"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10,0,5,10);  //right padding
        panelSuperiorCentro.add(origenCBox, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5,10,5,20);  //right padding
        panelSuperiorCentro.add(new JLabel("Destino"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5,0,5,10);  //right padding
        panelSuperiorCentro.add(destinoCBox, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5,10,5,20);  //right padding
        panelSuperiorCentro.add(new JLabel("Plano"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5,0,10,10);  //right padding
        panelSuperiorCentro.add(planoComboBox, c);
        
        
        
        
        
        panelSuperiorCentro.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(compoundBorder);
        JPanel panelIzqDcha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzqDcha.add(panelSuperiorCentro);
        panelCentro.add(panelIzqDcha, BorderLayout.NORTH);

        JPanel centradoVertical = new JPanel(new BorderLayout());

        JPanel mapsCentrado = new JPanel(new GridLayout());
        mapsCentrado.add(planoMetroDibujo);
        centradoVertical.add(mapsCentrado, BorderLayout.CENTER);
        panelCentro.add(centradoVertical, BorderLayout.CENTER);

        principal.add(panelCentro, BorderLayout.CENTER);
        container.add(principal);
    }

    private void mostrarRuta() {
        final Parada pInicial = (Parada) origenCBox.getSelectedItem();
        final Parada pFinal = (Parada) destinoCBox.getSelectedItem();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ruta<ParadaRutaConLinea> ruta = new BusquedaRutaConLinea(pInicial, pFinal).calcularRuta((BusquedaRuta.TipoRuta) tiposRutasCBox.getSelectedItem());
                rutaJTable.setModel(new TablaRutaModel(ruta));
                planoMetroDibujo.pintarRuta(ruta);
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
