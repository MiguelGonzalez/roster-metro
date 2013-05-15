package rostermetroswing;

import rostermetroswing.models.TablaRutaModel;
import rostermetro.cargaMapas.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.xml.sax.*;
import rostermetro.busqueda.*;
import rostermetro.busqueda.conLinea.*;
import rostermetro.domain.*;
import rostermetro.parsers.*;
import rostermetroswing.components.*;

/**
 * Aplicación Swing que utiliza nuestra librería RosterMetro para permitir al
 * usuario buscar una ruta entre dos paradas. Se muestran los resultados en una
 * tabla y en un mapa de google maps.
 *
 * @author Jaime Bárez y Miguel González
 */
public class RosterMetroSwing extends JFrame {

    public static final String TITULO = "Roster Metro";
    public static final Dimension MINIMUM = new Dimension(100, 100);
    private static final Color COLOR_RUTA_CALCULADA = new Color(0x228b22);
    private static final Color COLOR_CALCULANDO_RUTA = Color.RED;
    /**
     * Plano con el que estamos trabajando
     */
    private Plano plano;
    /**
     * JLabel que muestra el estado de la búsqueda
     */
    private JLabel jLabelCalculo;
    private JComboBox<Parada> origenCBox;
    private JComboBox<Parada> destinoCBox;
    private JComboBox<BusquedaRuta.TipoRuta> tiposRutasCBox;
    private JComboBox<CargaMapas.PlanoAlmacenado> planoComboBox;
    private JTable rutaJTable;
    private JProgressBar progressBar;
    private PlanoGoogleMaps planoGoogleMaps;
    private final PlanoParser planoParser;
    private final Executor ejecutorBusquedas;

    /**
     * Crea un JFrame RosterMetroSwing y lo hace visible
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RosterMetroSwing(TITULO) {
                    {
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        setMinimumSize(MINIMUM);
                        setSize(Toolkit.getDefaultToolkit().getScreenSize());
                        setLocationRelativeTo(null);
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                }.setVisible(true);

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
        ejecutorBusquedas = Executors.newSingleThreadExecutor();

        initComponents();

        initListeners();

        initInterfaz();

        setPlano((CargaMapas.PlanoAlmacenado) planoComboBox.getSelectedItem());
    }

    /**
     *
     * Inicializa los jCombobox, la tabla, el plano de Google Maps...
     */
    private void initComponents() {

        jLabelCalculo = new JLabel();

        planoComboBox = new JComboBox<>(new DefaultComboBoxModel<>(CargaMapas.getPlanosAlmacenados()));
        origenCBox = new JComboBox<>();
        AutoCompleteDecorator.decorate(origenCBox);

        destinoCBox = new JComboBox<>();
        AutoCompleteDecorator.decorate(destinoCBox);

        tiposRutasCBox = new JComboBox<>(BusquedaRuta.TipoRuta.values());

        rutaJTable = new JTable();

        planoGoogleMaps = new PlanoGoogleMaps();

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
    }

    /**
     * Comunica componentes y acciones
     */
    private void initListeners() {
        ActionListener muestraRuta = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularRuta();
            }
        };
        ActionListener setPlano = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setPlano((CargaMapas.PlanoAlmacenado) planoComboBox.getSelectedItem());

            }
        };
        origenCBox.addActionListener(muestraRuta);
        destinoCBox.addActionListener(muestraRuta);
        tiposRutasCBox.addActionListener(muestraRuta);
        planoComboBox.addActionListener(setPlano);
    }

    /**
     * Monta todos los componentes en paneles de forma ordenada
     */
    private void initInterfaz() {

        Container container = getContentPane();
        container.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearPanelIzquierda(), crearPanelDerecho()));
    }

    /**
     * Calcula la ruta entre paradas origen y destino. Se calculará en segundo
     * plano y se mostrará tras su cálculo
     */
    private void calcularRuta() {
        final Parada pInicial = getParadaOrigenSeleccionada();
        final Parada pFinal = getParadaDestinoSeleccionada();
        final BusquedaRuta.TipoRuta tr = getTipoRutaSeleccionada();

        ejecutorBusquedas.execute(new Runnable() {
            @Override
            public void run() {
                long time_start, time_end;
                mostrarCalculando();
                time_start = System.currentTimeMillis();
                BusquedaRutaConLinea busquedaRutaConLinea = new BusquedaRutaConLinea(pInicial, pFinal);
                mostrarRuta(busquedaRutaConLinea.calcularRuta(tr));
                time_end = System.currentTimeMillis();
                mostrarCalculada(time_end - time_start,busquedaRutaConLinea.getNodosExplorados());
            }
        });
    }

    /**
     * Avisa gráficamente de que estamos calculando la ruta
     */
    private void mostrarCalculando() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                jLabelCalculo.setText("Calculando...");
                jLabelCalculo.setForeground(COLOR_CALCULANDO_RUTA);
                progressBar.setValue(0);
            }
        });
    }

    /**
     * Avisamos gráficamente de que la ruta ha sido calculada
     *
     * @param milisegundos Tiempo que ha tardado la búsqueda
     * @param nodosExplorados Nodos que han sido explorados
     */
    private void mostrarCalculada(final long milisegundos, final int nodosExplorados) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                jLabelCalculo.setText("<html>Calculado en " + milisegundos + " ms<br>Nodos explorados: "+nodosExplorados+"</html>");
                jLabelCalculo.setForeground(COLOR_RUTA_CALCULADA);
                progressBar.setValue(100);
            }
        });
    }

    /**
     * Hace que los componentes muestren la ruta calculada
     *
     * @param ruta
     */
    private void mostrarRuta(final Ruta ruta) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                rutaJTable.setModel(new TablaRutaModel(ruta));
                planoGoogleMaps.mostrarRuta(ruta);
            }
        });
    }

    /**
     * Cambia el plano, y como consecuencia actualiza los modelos de los
     * componentes
     *
     * @param planoAMostrar
     */
    private void setPlano(CargaMapas.PlanoAlmacenado planoAMostrar) {
        try {
            plano = planoParser.parse(CargaMapas.getPlanoAsStream(planoAMostrar));
        } catch (IOException | SAXException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error",
                    "No se pudo cargar el mapa de metro",
                    JOptionPane.ERROR_MESSAGE);

            Logger.getLogger(RosterMetroSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
        setModels();
        calcularRuta();
    }

    /**
     * Establece los modelos de los componentes con respecto al plano
     */
    private void setModels() {
        Collection<Parada> coleccionParadas = plano.getParadas();
        Parada[] paradas = coleccionParadas.toArray(new Parada[coleccionParadas.size()]);
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

    /**
     * Devuelve el panel izquierdo creado
     *
     * @return
     */
    private JPanel crearPanelIzquierda() {
        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBorder(crearCompoundBorder());

        JPanel supDcho = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 10, 5, 20);
        supDcho.add(new JLabel("Tipo de ruta"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 5, 10);
        supDcho.add(tiposRutasCBox, c);

        JPanel alineadorNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alineadorNorte.add(supDcho);
        alineadorNorte.setBorder(crearCompoundBorder());

        izquierda.add(alineadorNorte, BorderLayout.NORTH);

        izquierda.add(progressBar, BorderLayout.SOUTH);

        izquierda.add(new JScrollPane(rutaJTable), BorderLayout.CENTER);

        return izquierda;
    }

    /**
     * Crea un borde compuesto
     *
     * @return
     */
    private Border crearCompoundBorder() {
        Border bevelBrdr = new BevelBorder(BevelBorder.LOWERED);
        Border emptyBrdr = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBrdr = new CompoundBorder(emptyBrdr, bevelBrdr);
        return compoundBrdr;
    }

    /**
     * Crea el panel superior derecho
     *
     * @return
     */
    private JPanel crearPanelSuperiorDerecho() {
        JPanel supDcho = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 10, 5, 20);
        supDcho.add(new JLabel("Origen"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 5, 10);
        supDcho.add(origenCBox, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5, 10, 5, 20);
        supDcho.add(new JLabel("Destino"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5, 0, 5, 10);
        supDcho.add(destinoCBox, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5, 10, 5, 20);
        supDcho.add(new JLabel("Plano"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5, 0, 10, 10);  //right padding
        supDcho.add(planoComboBox, c);

        supDcho.setBorder(new BevelBorder(BevelBorder.LOWERED));
        return supDcho;
    }

    /**
     * Crea el panel derecho
     *
     * @return
     */
    private Component crearPanelDerecho() {

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(crearCompoundBorder());

        JPanel alineadorNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alineadorNorte.add(crearPanelSuperiorDerecho());
        alineadorNorte.add(jLabelCalculo);
        panelDerecho.add(alineadorNorte, BorderLayout.NORTH);

        JPanel centrarPlanoMaps = new JPanel(new GridBagLayout());
        centrarPlanoMaps.add(planoGoogleMaps);
        planoGoogleMaps.setBorder(new EtchedBorder(EtchedBorder.RAISED));

        panelDerecho.add(centrarPlanoMaps, BorderLayout.CENTER);
        return panelDerecho;
    }

    private Parada getParadaOrigenSeleccionada() {
        return origenCBox.getItemAt(origenCBox.getSelectedIndex());
    }

    private Parada getParadaDestinoSeleccionada() {
        return destinoCBox.getItemAt(destinoCBox.getSelectedIndex());
    }

    private BusquedaRuta.TipoRuta getTipoRutaSeleccionada() {
        return tiposRutasCBox.getItemAt(tiposRutasCBox.getSelectedIndex());
    }
}
