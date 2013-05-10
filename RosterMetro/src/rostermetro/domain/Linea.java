package rostermetro.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Linea {

    public static final String CIRCULAR = "Circular";
    private final String nombre;
    private final List<Parada> paradas;
    private final boolean circular;

    public Linea(String nombre) {
        this.nombre = nombre;
        this.paradas = new ArrayList<>();
        circular = nombre.endsWith(CIRCULAR);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Parada> getParadas() {
        return Collections.unmodifiableList(paradas);
    }

    private boolean tieneAnterior(Parada parada) {
        if(circular){
            return true;
        } else{
            return paradas.indexOf(parada) > 0;
        }
    }

    private Parada getAnteriorParada(Parada parada) {
        int index = paradas.indexOf(parada) - 1;
        if(circular && index<0){
            index =paradas.size()+index;
        }
        return paradas.get(index);
    }

    private boolean tieneSiguiente(Parada parada) {
        if(circular){
            return true;
        } else{
            return paradas.indexOf(parada) != paradas.size() - 1;
        }
    }

    private Parada getSiguienteParada(Parada parada) {
        int index = paradas.indexOf(parada) + 1;
        if(circular && index>=paradas.size()){
            index =index%paradas.size();
        }
        return paradas.get(index);

    }

    public List<Parada> getParadasQueRodean(Parada parada) {
        List<Parada> sucesores = new LinkedList<>();
        if (tieneAnterior(parada)) {
            sucesores.add(getAnteriorParada(parada));
        }
        if (tieneSiguiente(parada)) {
            sucesores.add(getSiguienteParada(parada));
        }
        return sucesores;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Linea other = (Linea) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void addParada(Parada paradaToAdd) {
        paradas.add(paradaToAdd);
    }
}
