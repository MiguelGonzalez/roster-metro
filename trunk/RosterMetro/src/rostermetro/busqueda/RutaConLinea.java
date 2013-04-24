package rostermetro.busqueda;

import java.util.Arrays;
import java.util.List;
import rostermetro.domain.ParadaRuta;

public class RutaConLinea {
    
    private List<ParadaRuta> paradasRuta;
    private BusquedaRutaConLinea busquedaConLinea;
    private boolean buscadoParadas = false;
    
    public RutaConLinea(Ruta ruta) {
        busquedaConLinea = 
                new BusquedaRutaConLinea(ruta);
        paradasRuta = busquedaConLinea.getRutaConLinea();
    }
    
    public List<ParadaRuta> getParadasRuta() {
        return paradasRuta;
    }
    
    @Override
    public String toString() {
        return "Ruta: \n" + Arrays.toString(paradasRuta.toArray());
    }
}
