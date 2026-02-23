/** Clasa pt conectarea la baza de date Microsoft SQL Server
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String url =
            "jdbc:sqlserver://LAPTOP-01948VRS\\SQLEXPRESS01:1433;databaseName=Clase_Elevi;encrypt=true;trustServerCertificate=true";
    private static final String user ="cismaru";
    private static final String password ="trabantro";

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url, user, password);
        }
        catch(Exception e){
            throw new RuntimeException("Conexiunea SQL esuata: " + e.getMessage(), e);
        }
    }
}

