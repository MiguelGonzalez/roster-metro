/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro.auxiliares;

import java.util.ArrayList;
import java.util.List;
import rostermetro.domain.Parada;

public class ADatosFilaAsterisco implements Comparable<ADatosFilaAsterisco> {
    private Parada clave;
    private ADatosFilaAsterisco anterior;
    private Parada paradaFinal;

    public ADatosFilaAsterisco(Parada clave, ADatosFilaAsterisco anterior, Parada paradaFinal) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
    }

    public double getH() {
        return clave.getDistancia(paradaFinal);
    }
    
    public double getG() {
        if(anterior == null) {
            return 0;
        } else {
            return anterior.getG() + clave.getDistancia(anterior.getClave());
        }
    }
    
    public double getF() {
        return getH() + getG();
    }
    
    public Parada getClave() {
        return clave;
    }

    public ADatosFilaAsterisco getAnterior() {
        return anterior;
    }
    
    public List<ADatosFilaAsterisco> getSucesores() {
        List<ADatosFilaAsterisco> sucesores = new ArrayList<>();
        for (Parada parada : clave.getSucesores()) {
            ADatosFilaAsterisco sucesor = new ADatosFilaAsterisco(
                        parada, this, paradaFinal);
            sucesores.add(sucesor);
        }
        return sucesores;
    }
    
    @Override
    public int compareTo(ADatosFilaAsterisco aDatosFilaAsterisco) {
        double result = getH() - aDatosFilaAsterisco.getH();
        if(result == 0) {
            return 0;
        } else if(result > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
