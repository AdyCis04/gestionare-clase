/** Clasa pt rularea proghramului
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.gestionare_clase.DatabaseConnection;

import java.sql.Connection;

@SpringBootApplication
public class GestionareClaseApplication {

	public static void main(String[] args) {

        try {
            Connection connection = DatabaseConnection.getConnection();
            System.out.println("Conexiune la baza de date reușită!");
        } catch (Exception e) {
            System.err.println("Eroare la conexiune: " + e.getMessage());
        }
		SpringApplication.run(GestionareClaseApplication.class, args);
	}

}
