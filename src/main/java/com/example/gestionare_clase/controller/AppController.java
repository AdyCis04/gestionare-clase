/** Clasa pentru controller clasele Mapping principale
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.gestionare_clase.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;

import com.example.gestionare_clase.Functions;

@Controller
public class AppController {
    @GetMapping("/")
    public String index(Model model) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        String sql1 = "SELECT COUNT(*) FROM elevi";
        String sql2 = "SELECT COUNT(*) FROM clase";

        PreparedStatement ps1 = con.prepareStatement(sql1);
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ResultSet rs1 = ps1.executeQuery();
        ResultSet rs2 = ps2.executeQuery();
        rs1.next();
        rs2.next();
        model.addAttribute("totalElevi", rs1.getInt(1));
        model.addAttribute("totalClase", rs2.getInt(1));
        return "index";
    }


    @GetMapping("/login")
    public String login() {

        return "login";
    }


}
