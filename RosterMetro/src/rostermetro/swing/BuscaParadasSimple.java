/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.swing;

import com.sun.org.apache.bcel.internal.generic.FLOAD;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
        ImageIcon metroIcon = new ImageIcon(BuscaParadasSimple.class.getResource("metroMadrid.png"));
        JLabel jLabel = new JLabel(metroIcon);
        //jLabel.setPreferredSize(new Dimension(500, 500));
        //derecha.add(jLabel, BorderLayout.SOUTH);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Ruta ruta = planoMetro.getRuta((Parada) origenComboBox.getSelectedItem(), (Parada) destinoComboBox.getSelectedItem());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


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

                    }
                });
            }
        }).start();
    }
}
