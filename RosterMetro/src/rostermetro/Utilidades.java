/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rostermetro;

/**
 *
 * @author ceura
 */
public class Utilidades {

    public static void appendLine(StringBuilder str, String... appends) {
        for (String append : appends) {
            str.append(append);
        }
        str.append("\n");
    }
}
