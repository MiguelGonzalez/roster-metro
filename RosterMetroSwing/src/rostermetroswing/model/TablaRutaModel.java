package rostermetroswing.model;

import javax.swing.table.AbstractTableModel;
import rostermetro.busqueda.Ruta;

/**
 *
 * @author Ceura
 */
public class TablaRutaModel extends AbstractTableModel {

    private final Ruta ruta;
    
    public TablaRutaModel(Ruta ruta) {
        this.ruta = ruta;
    }
    
    @Override
    public int getRowCount() {
        return ruta!=null?ruta.getListadoParadas().size():1;
    }
    
    @Override
    public int getColumnCount() {
        return 1;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ruta!=null?ruta.getListadoParadas().get(rowIndex):"No existe ruta";
    }
    
    @Override
    public String getColumnName(int column) {
        
        return "Ruta";
    }
}
