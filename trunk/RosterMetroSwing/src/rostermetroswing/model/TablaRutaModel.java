package rostermetroswing.model;

import javax.swing.table.AbstractTableModel;
import rostermetro.busqueda.commons.Ruta;
import rostermetro.busqueda.conLinea.ParadaRutaConLinea;
import rostermetro.domain.Linea;

/**
 *
 * @author Ceura
 */
public class TablaRutaModel extends AbstractTableModel {
    private final Ruta<ParadaRutaConLinea> ruta;

    
    public TablaRutaModel(Ruta<ParadaRutaConLinea> ruta) {
        this.ruta = ruta;
    }
    
    @Override
    public int getRowCount() {
        return ruta!=null?ruta.getListadoParadas().size():0;
    }
    
    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return ruta!=null?ruta.getListadoParadas().get(rowIndex).getNombre():"No existe ruta";
        } else {
            if(ruta==null){
                return "";
            } else{
                Linea linea = ruta.getListadoParadas().get(rowIndex).getLinea();
                if(linea ==null){
                    return "--------";
                } else{
                    return linea.getNombre();
                }
            }
        }
    }
    
    @Override
    public String getColumnName(int column) {
        if(column==0) {
            return "Paradas";
        } else {
            return "Línea a coger";
        }
    }
}
