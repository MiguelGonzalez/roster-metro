package rostermetro.auxiliares;

import java.util.ArrayList;
import java.util.List;
import rostermetro.domain.Parada;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class FilaAAsterisco implements Comparable<FilaAAsterisco> {

    private Parada clave;
    private FilaAAsterisco anterior;
    private Parada paradaFinal;

    public FilaAAsterisco(Parada clave, FilaAAsterisco anterior, Parada paradaFinal) {
        this.clave = clave;
        this.anterior = anterior;
        this.paradaFinal = paradaFinal;
    }

    public double getH() {
        return clave.getDistancia(paradaFinal);
    }

    public double getG() {
        if (anterior == null) {
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

    public FilaAAsterisco getAnterior() {
        return anterior;
    }

    public List<FilaAAsterisco> getSucesores() {
        List<FilaAAsterisco> sucesores = new ArrayList<>();
        for (Parada parada : clave.getSucesores()) {
            FilaAAsterisco sucesor = new FilaAAsterisco(
                    parada, this, paradaFinal);
            sucesores.add(sucesor);
        }
        return sucesores;
    }

    @Override
    public int compareTo(FilaAAsterisco compareTo) {
        double result = getF() - compareTo.getF();
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
