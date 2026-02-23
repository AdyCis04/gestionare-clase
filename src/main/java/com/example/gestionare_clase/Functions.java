/** Clasa cu diverse functii de verificare a String-urilor
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase;

public class Functions {
    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty())
            return false;
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
