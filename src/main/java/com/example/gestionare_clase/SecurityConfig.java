/** Clasa pt criptare
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mindrot.jbcrypt.BCrypt;

@Configuration
public class SecurityConfig {

    // Funcție care encodează parola
    public static String encode(String parola) {
        return BCrypt.hashpw(parola, BCrypt.gensalt());
    }

    // Funcție care verifică parola
    public static boolean matches(String parola, String hash) {
        return BCrypt.checkpw(parola, hash);
    }
}
