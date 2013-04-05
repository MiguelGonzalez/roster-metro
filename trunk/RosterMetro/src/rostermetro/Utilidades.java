package rostermetro;

/**
 *
 * @author Jaime Bárez y Miguel González
 */
public class Utilidades {

    public static void appendLine(StringBuilder str, String... appends) {
        for (String append : appends) {
            str.append(append);
        }
        str.append("\n");
    }
}
