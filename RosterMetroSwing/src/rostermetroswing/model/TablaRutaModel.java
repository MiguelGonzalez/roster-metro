/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetroswing.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import rostermetro.domain.Parada;
import rostermetro.domain.Ruta;
import rostermetroswing.axiliares.HashCodeUtil;

/**
 *
 * @author Ceura
 */
public class TablaRutaModel extends AbstractTableModel {

    private HashMap<Point, Parada> lookup;
    private int rows;
    private int columns;
    private String headers[];

    public TablaRutaModel() {
        headers = new String[]{"Paradas"};
        lookup = new HashMap<>();
        
        rows = lookup.size();
        columns = headers.length;
    }
    
    public TablaRutaModel(Ruta ruta) {
        super();
        
        //Rellenamos los datos
        for (Iterator<Parada> it = ruta.getListadoParadas().iterator(); it.hasNext();) {
            Parada parada = it.next();
            
            lookup.put(new Point(lookup.size(), 0), parada);
            
        }
        
        rows = lookup.size();
        columns = headers.length;
    }
    
    @Override
    public boolean equals(Object tablaRutaModel) {
        boolean columnasIguales = true;
        
        if(tablaRutaModel instanceof TablaRutaModel) {
            TablaRutaModel p = (TablaRutaModel) tablaRutaModel;

            if(p.getColumnCount() == columns && p.getRowCount() == rows) {
                for(int i=0 ; i<columns; i++) {
                    if(!p.getColumnName(i).equals(headers[i])) {
                        columnasIguales = false;
                    }
                }
            }
        }
        
        
        if(columnasIguales) {
            TablaRutaModel p = (TablaRutaModel) tablaRutaModel;
            //Comprobamos si tienen los mismos datos
            for(int i=0; i<rows; i++) {
                for(int j=0; j<columns; j++) {
                    Point point = new Point(i, j);
                    //Si no es el mismo dato devolvemos false
                    if(!lookup.get(point).equals(p.getValueAt(i, j))) {
                        return false;
                    }
                }
            }
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = HashCodeUtil.SEED;
        
        result = HashCodeUtil.hash(result, rows);
        result = HashCodeUtil.hash(result, columns);
        result = HashCodeUtil.hash(result, headers);
        for(int i=0; i<rows; i++) {
            for(int j=0; j<columns; j++) {
                result = HashCodeUtil.hash(result, lookup.get(new Point(i, j)));
            }
        }
        
        return result;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        return lookup.get(new Point(row, column));
    }

    public void setValueAt(Parada parada, int row, int column) {
        if ((rows < 0) || (columns < 0)) {
            throw new IllegalArgumentException("Invalid row/column setting");
        }
        if ((row < rows) && (column < columns)) {
            lookup.put(new Point(row, column), parada);
        }
        
        super.fireTableDataChanged();
    }
    
    public void replaceRuta(Ruta ruta) {
        lookup.clear();
        
        //Rellenamos los datos
        for (Iterator<Parada> it = ruta.getListadoParadas().iterator(); it.hasNext();) {
            Parada parada = it.next();
            
            lookup.put(new Point(lookup.size(), 0), parada);
            
        }
        
        rows = lookup.size();
        
        super.fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
