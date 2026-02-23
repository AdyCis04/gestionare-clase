/** Clasa cu functii pentru autentificare
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase.controller;

import com.example.gestionare_clase.DatabaseConnection;
import com.example.gestionare_clase.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
public class AuthController {

    @GetMapping("/session")
    public String session(@RequestParam String username,
                          @RequestParam String password,
                          Model model,
                          HttpServletRequest request) throws SQLException {

        // validare simplă
        model.addAttribute("username", username);
        if (username != null)
            username = username.trim();

        boolean validUsername = username != null && !username.isEmpty();
        boolean validPassword = password != null && !password.isEmpty();

        model.addAttribute("valid10", validUsername);
        model.addAttribute("valid20", validPassword);

        if (!validUsername || !validPassword) {
            return "login";
        }

        Connection con = DatabaseConnection.getConnection();
        if (username.equals("admin"))
        {
            model.addAttribute("valid11", true);
            String sqlPassAdmin = "SELECT parola FROM Administrator";
            PreparedStatement ps = con.prepareStatement(sqlPassAdmin);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String encodedPass = rs.getNString(1);

            rs.close();
            ps.close();
            con.close();

            if (SecurityConfig.matches(password, encodedPass)) {
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("role", "Administrator");
                model.addAttribute("message", "");
                model.addAttribute("valid21", true);

                Connection con1 = DatabaseConnection.getConnection();
                String sql1 = "SELECT COUNT(*) FROM elevi";
                String sql2 = "SELECT COUNT(*) FROM clase";

                PreparedStatement ps11 = con1.prepareStatement(sql1);
                PreparedStatement ps21 = con1.prepareStatement(sql2);
                ResultSet rs11 = ps11.executeQuery();
                ResultSet rs21 = ps21.executeQuery();
                rs11.next();
                rs21.next();
                model.addAttribute("totalElevi", rs11.getInt(1));
                model.addAttribute("totalClase", rs21.getInt(1));

                return "index";
            }
            else
            {
                model.addAttribute("message", "Parola incorecta");
                model.addAttribute("valid21", false);
                return "login";
            }
        }
        else
        {
            String sqlElev = "SELECT nume, prenume, email, parola FROM elevi WHERE email = ?";
            PreparedStatement ps1 = con.prepareStatement(sqlElev);
            ps1.setNString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            boolean exists = false;

            if (rs1.next())
            {
                model.addAttribute("valid11", true);
                String nume = rs1.getNString("nume");
                String prenume = rs1.getNString("prenume");
                exists = true;
                String parolaEncoded = "";
                parolaEncoded = rs1.getNString("parola");

                rs1.close();
                ps1.close();

                if (SecurityConfig.matches(password, parolaEncoded)) {
                    request.getSession().setAttribute("username", nume + prenume);
                    request.getSession().setAttribute("role", "Elev");
                    model.addAttribute("message", "");
                    model.addAttribute("valid21", true);
                    con.close();

                    Connection con1 = DatabaseConnection.getConnection();
                    String sql1 = "SELECT COUNT(*) FROM elevi";
                    String sql2 = "SELECT COUNT(*) FROM clase";

                    PreparedStatement ps11 = con1.prepareStatement(sql1);
                    PreparedStatement ps21 = con1.prepareStatement(sql2);
                    ResultSet rs11 = ps11.executeQuery();
                    ResultSet rs21 = ps21.executeQuery();
                    rs11.next();
                    rs21.next();
                    model.addAttribute("totalElevi", rs11.getInt(1));
                    model.addAttribute("totalClase", rs21.getInt(1));

                    return "index";
                }
                else
                {
                    model.addAttribute("message", "Parola incorecta");
                    model.addAttribute("valid21", false);
                    con.close();
                    return "login";
                }
            }

            String sqlProf = "SELECT nume, prenume, email, parola FROM profesori WHERE email = ?";
            PreparedStatement ps2 = con.prepareStatement(sqlProf);
            ps2.setNString(1, username);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next())
            {
                model.addAttribute("valid11", true);
                String nume = rs2.getNString("nume");
                String prenume = rs2.getNString("prenume");
                exists = true;
                String parolaEncoded = "";
                parolaEncoded = rs2.getNString("parola");

                rs2.close();
                ps2.close();

                if (SecurityConfig.matches(password, parolaEncoded)) {
                    request.getSession().setAttribute("username", nume + prenume);
                    request.getSession().setAttribute("role", "Profesor");
                    model.addAttribute("message", "");
                    model.addAttribute("valid21", true);
                    con.close();

                    Connection con1 = DatabaseConnection.getConnection();
                    String sql1 = "SELECT COUNT(*) FROM elevi";
                    String sql2 = "SELECT COUNT(*) FROM clase";

                    PreparedStatement ps11 = con1.prepareStatement(sql1);
                    PreparedStatement ps21 = con1.prepareStatement(sql2);
                    ResultSet rs11 = ps11.executeQuery();
                    ResultSet rs21 = ps21.executeQuery();
                    rs11.next();
                    rs21.next();
                    model.addAttribute("totalElevi", rs11.getInt(1));
                    model.addAttribute("totalClase", rs21.getInt(1));

                    return "index";
                }
                else
                {
                    model.addAttribute("message", "Parola incorecta");
                    model.addAttribute("valid21", false);
                    con.close();
                    return "login";
                }
            }

            if (!exists) {
                model.addAttribute("valid11", false);
                model.addAttribute("message", "Nu exista utilizator cu acest username.");
                con.close();
                return "login";
            }
        }

        Connection con1 = DatabaseConnection.getConnection();
        String sql1 = "SELECT COUNT(*) FROM elevi";
        String sql2 = "SELECT COUNT(*) FROM clase";

        PreparedStatement ps11 = con1.prepareStatement(sql1);
        PreparedStatement ps21 = con1.prepareStatement(sql2);
        ResultSet rs11 = ps11.executeQuery();
        ResultSet rs21 = ps21.executeQuery();
        rs11.next();
        rs21.next();
        model.addAttribute("totalElevi", rs11.getInt(1));
        model.addAttribute("totalClase", rs21.getInt(1));

        return "index";
    }

    @PostMapping("/endSession")
    public String endSession(HttpServletRequest request, Model model) throws SQLException
    {
        request.getSession().invalidate();

        Connection con1 = DatabaseConnection.getConnection();
        String sql1 = "SELECT COUNT(*) FROM elevi";
        String sql2 = "SELECT COUNT(*) FROM clase";

        PreparedStatement ps1 = con1.prepareStatement(sql1);
        PreparedStatement ps2 = con1.prepareStatement(sql2);
        ResultSet rs1 = ps1.executeQuery();
        ResultSet rs2 = ps2.executeQuery();
        rs1.next();
        rs2.next();
        model.addAttribute("totalElevi", rs1.getInt(1));
        model.addAttribute("totalClase", rs2.getInt(1));

        return "index";
    }
}
