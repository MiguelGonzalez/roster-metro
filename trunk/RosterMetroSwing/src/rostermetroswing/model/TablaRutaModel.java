package rostermetroswing.model;

import javax.swing.table.AbstractTableModel;
import rostermetro.busqueda.RutaConLinea;

/**
 *
 * @author Ceura
 */
public class TablaRutaModel extends AbstractTableModel {

    private final RutaConLinea ruta;
    
    public TablaRutaModel(RutaConLinea ruta) {
        this.ruta = ruta;
    }
    
    @Override
    public int getRowCount() {
        return ruta.getParadasRuta()!=null?ruta.getParadasRuta().size():1;
    }
    
    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return ruta.getParadasRuta()!=null?ruta.getParadasRuta().get(rowIndex).getNombre():"No existe ruta";
        } else {
            return ruta.getParadasRuta()!=null?ruta.getParadasRuta().get(rowIndex).getLineaSiguiente():"";
        }
    }
    
    @Override
    public String getColumnName(int column) {
        if(column==0) {
            return "Ruta";
        } else {
            return "Línea a coger";
        }
    }
}
