/** Clasa controller pt fisierele html din folderul Elev
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase.controller;

import com.example.gestionare_clase.DatabaseConnection;
import com.example.gestionare_clase.Functions;
import com.example.gestionare_clase.SecurityConfig;
import com.example.gestionare_clase.classes.Clasa;
import com.example.gestionare_clase.classes.Elev;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ElevController {
    @GetMapping("/eleviIndex")
    public String eleviIndex (
            @RequestParam("id") int clasa_id,
            Model model) throws SQLException {
        List<Elev> elevi = new ArrayList<>();

        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM elevi WHERE clasa_id = ?");
        ps.setInt(1, clasa_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Elev elev1 = new Elev();

            elev1.setId(rs.getInt("elev_id"));
            elev1.setNume(rs.getString("nume"));
            elev1.setPrenume(rs.getString("prenume"));
            elev1.setCnp(rs.getString("cnp"));
            elev1.setEmail(rs.getString("email"));
            elev1.setClasaId(rs.getInt("clasa_id"));
            elevi.add(elev1);
        }
        model.addAttribute("clasa_id", clasa_id);
        model.addAttribute("elevi", elevi);
        return "Elev/index";
    }

    @GetMapping("/eleviCreate")
    public String eleviCreate(
            @RequestParam("clasa_id") int clasa_id,
            Model model)
    {
        model.addAttribute("messageValue", -1);

        model.addAttribute("nume", "");
        model.addAttribute("prenume", "");
        model.addAttribute("cnp", "");
        model.addAttribute("email", "");
        model.addAttribute("parola", "");
        model.addAttribute("clasa_id", clasa_id);
        return "Elev/create";
    }

    @PostMapping("/eleviCreatePost")
    public String eleviCreatePost(
            @RequestParam("nume") String nume,
            @RequestParam("prenume") String prenume,
            @RequestParam("cnp") String cnp,
            @RequestParam("email") String email,
            @RequestParam("parola") String parola,
            @RequestParam("clasa_id") int clasa_id,
            Model model
    ) throws SQLException {
        boolean[] valid1 = new boolean[2];
        boolean[] valid2 = new boolean[2];
        boolean[] valid3 = new boolean[2];
        boolean[] valid4 = new boolean[2];
        boolean[] valid5 = new boolean[2];

        int messageValue = -1;
        String message = "";

        nume = nume.trim();
        prenume = prenume.trim();
        cnp = cnp.trim();
        email = email.trim();
        parola = parola.trim();

        //verificare nume
        if (nume == null || nume.trim().isEmpty())
        {
            valid1[0] = false;
            valid1[1] = false;
        }
        else
        {
            valid1[0] = true;
            valid1[1] = true;

            for (int ord = 0; ord < nume.length(); ord++)
            {
                if (ord > 0) {
                    if (!('a' <= nume.charAt(ord) && nume.charAt(ord) <= 'z')) {
                        valid1[1] = false;
                        break;
                    }
                }
                else {
                    if (!('A' <= nume.charAt(ord) && nume.charAt(ord) <= 'Z')) {
                        valid1[1] = false;
                        break;
                    }
                }
            }
        }

        //verificare prenume
        if (prenume == null || prenume.trim().isEmpty())
        {
            valid2[0] = false;
            valid2[1] = false;
        }
        else
        {
            valid2[0] = true;
            valid2[1] = true;

            for (int ord = 0; ord < prenume.length(); ord++)
            {
                if (ord > 0) {
                    if (!('a' <= prenume.charAt(ord) && prenume.charAt(ord) <= 'z')) {
                        valid2[1] = false;
                        break;
                    }
                }
                else {
                    if (!('A' <= prenume.charAt(ord) && prenume.charAt(ord) <= 'Z')) {
                        valid2[1] = false;
                        break;
                    }
                }
            }
        }

        if (cnp == null || cnp.isEmpty())
        {
            valid3[0] = false;
            valid3[1] = false;
        }
        else
        {
            valid3[0] = true;
            valid3[1] = true;

            for (char chr : cnp.toCharArray())
            {
                if (!('0' <= chr && chr <= '9')) {
                    valid3[1] = false;
                    break;
                }
            }
            if (cnp.length() != 13)
                valid3[1] = false;
        }

        // verificare email (format strict)
        if (email == null || email.isEmpty()) {
            valid4[0] = false;
            valid4[1] = false;
        } else {
            valid4[0] = true;
            valid4[1] = email.matches(
                    "^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*@([A-Za-z][A-Za-z0-9]*\\.)+ro$"
            );
        }

        // verificare parola
        if (parola == null || parola.isEmpty()) {
            valid5[0] = false;
            valid5[1] = false;
        } else {
            valid5[0] = true;
            valid5[1] = parola.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$");
        }


        if (valid1[0] &&  valid1[1] &&
                valid2[0] && valid2[1] &&
                valid3[0] && valid3[1] &&
                valid4[0] && valid4[1] &&
                valid5[0] && valid5[1]) {

            Connection con = DatabaseConnection.getConnection();

            String checkSql1 = "SELECT count(*) FROM elevi WHERE cnp = ?";
            String checkSql2 = "SELECT count(*) FROM elevi WHERE email = ?";
            String checkSql3 = "SELECT count(*) FROM profesori WHERE cnp = ?";
            String checkSql4 = "SELECT count(*) FROM profesori WHERE email = ?";
            String sql = "INSERT INTO elevi(nume, prenume, cnp, email, parola, clasa_id) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps1 = con.prepareStatement(checkSql1);
            PreparedStatement ps2 = con.prepareStatement(checkSql2);
            PreparedStatement ps3 = con.prepareStatement(checkSql3);
            PreparedStatement ps4 = con.prepareStatement(checkSql4);
            ps1.setNString(1, cnp);
            ps2.setNString(1, email);
            ps3.setNString(1, cnp);
            ps4.setNString(1, email);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            ResultSet rs3 = ps3.executeQuery();
            ResultSet rs4 = ps4.executeQuery();

            rs1.next();
            int count1 = rs1.getInt(1);
            rs1.close();
            ps1.close();

            rs2.next();
            int count2 = rs2.getInt(1);
            rs2.close();
            ps2.close();

            rs3.next();
            int count3 = rs3.getInt(1);
            rs3.close();
            ps3.close();

            rs4.next();
            int count4 = rs4.getInt(1);
            rs4.close();
            ps4.close();

            if (count1 == 0 && count3 == 0 && count2 == 0 && count4 == 0)
            {
                PreparedStatement psIntro = con.prepareStatement(sql);

                psIntro.setNString(1, nume);
                psIntro.setNString(2, prenume);
                psIntro.setNString(3, cnp);

                psIntro.setNString(4, email);

                String encodedPass = SecurityConfig.encode(parola);
                psIntro.setString(5, encodedPass);
                psIntro.setInt(6, clasa_id);

                psIntro.executeUpdate();
                psIntro.close();

                messageValue = 1;
                message = "Elev adaugat cu succes :)";
            }
            else
            {
                if (count1 != 0 || count3 != 0)
                {
                    messageValue = 2;
                    message = "Exista deja o persoana cu CNP-ul introdus de tine";
                }
                if (count2 != 0 || count4 != 0)
                {
                    messageValue = 3;
                    message = "Exista deja o persoana cu adresa de e-mail introdusa de tine";
                }

            }
        }
        model.addAttribute("valid10", valid1[0]);
        model.addAttribute("valid11", valid1[1]);
        model.addAttribute("valid20", valid2[0]);
        model.addAttribute("valid21", valid2[1]);
        model.addAttribute("valid30", valid3[0]);
        model.addAttribute("valid31", valid3[1]);
        model.addAttribute("valid40", valid4[0]);
        model.addAttribute("valid41", valid4[1]);
        model.addAttribute("valid50", valid5[0]);
        model.addAttribute("valid51", valid5[1]);
        model.addAttribute("messageValue", messageValue);
        model.addAttribute("message", message);

        if (valid1[0] &&  valid1[1] &&
                valid2[0] && valid2[1] &&
                valid3[0] && valid3[1] &&
                valid4[0] && valid4[1] &&
                valid5[0] && valid5[1] && messageValue == 1)
        {
            model.addAttribute("nume", "");
            model.addAttribute("prenume", "");
            model.addAttribute("cnp", "");
            model.addAttribute("email", "");
            model.addAttribute("parola", "");
        }
        else
        {
            model.addAttribute("nume", nume);
            model.addAttribute("prenume", prenume);
            model.addAttribute("cnp", cnp);
            model.addAttribute("email", email);
            model.addAttribute("parola", parola);
        }
        model.addAttribute("clasa_id", clasa_id);
        return "Elev/create";
    }

    @GetMapping("/eleviModify")
    public String eleviModify(
            @RequestParam("clasa_id") Integer clasa_id,
            @RequestParam("elev_id") Integer elev_id,
            Model model) throws SQLException
    {

        String sql = "SELECT elev_id, nume, prenume, cnp, email FROM elevi WHERE elev_id = ?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, elev_id);
        ResultSet rs = ps.executeQuery();
        Elev elev1 = new Elev();
        while (rs.next()) {

            elev1.setId(rs.getInt("elev_id"));
            elev1.setNume(rs.getString("nume"));
            elev1.setPrenume(rs.getString("prenume"));
            elev1.setCnp(rs.getString("cnp"));
            elev1.setEmail(rs.getString("email"));
        }
        ps.close();

        model.addAttribute("elev_id", elev1.getId());
        model.addAttribute("nume", elev1.getNume());
        model.addAttribute("prenume", elev1.getPrenume());
        model.addAttribute("cnp", elev1.getCnp());
        model.addAttribute("email", elev1.getEmail());
        model.addAttribute("clasa_id", clasa_id);

        return "Elev/modify";
    }

    @PostMapping("/eleviModifyPost")
    public String eleviModifyPost(
            @RequestParam("elev_id") int elev_id,
            @RequestParam("clasa_id") int clasa_id,
            @RequestParam("nume") String nume,
            @RequestParam("prenume") String prenume,
            @RequestParam("cnp") String cnp,
            @RequestParam("email") String email,
            Model model
    ) throws SQLException
    {
        boolean[] valid1 = new boolean[2];
        boolean[] valid2 = new boolean[2];
        boolean[] valid3 = new boolean[2];
        boolean[] valid4 = new boolean[2];

        int messageValue = -1;
        String message = "";

        nume = nume.trim();
        prenume = prenume.trim();
        cnp = cnp.trim();
        email = email.trim();

        //verificare nume
        if (nume == null || nume.trim().isEmpty())
        {
            valid1[0] = false;
            valid1[1] = false;
        }
        else
        {
            valid1[0] = true;
            valid1[1] = true;

            for (int ord = 0; ord < nume.length(); ord++)
            {
                if (ord > 0) {
                    if (!('a' <= nume.charAt(ord) && nume.charAt(ord) <= 'z')) {
                        valid1[1] = false;
                        break;
                    }
                }
                else {
                    if (!('A' <= nume.charAt(ord) && nume.charAt(ord) <= 'Z')) {
                        valid1[1] = false;
                        break;
                    }
                }
            }
        }

        //verificare prenume
        if (prenume == null || prenume.trim().isEmpty())
        {
            valid2[0] = false;
            valid2[1] = false;
        }
        else
        {
            valid2[0] = true;
            valid2[1] = true;

            for (int ord = 0; ord < prenume.length(); ord++)
            {
                if (ord > 0) {
                    if (!('a' <= prenume.charAt(ord) && prenume.charAt(ord) <= 'z')) {
                        valid2[1] = false;
                        break;
                    }
                }
                else {
                    if (!('A' <= prenume.charAt(ord) && prenume.charAt(ord) <= 'Z')) {
                        valid2[1] = false;
                        break;
                    }
                }
            }
        }

        if (cnp == null || cnp.isEmpty())
        {
            valid3[0] = false;
            valid3[1] = false;
        }
        else
        {
            valid3[0] = true;
            valid3[1] = true;

            for (char chr : cnp.toCharArray())
            {
                if (!('0' <= chr && chr <= '9')) {
                    valid3[1] = false;
                    break;
                }
            }
            if (cnp.length() != 13)
                valid3[1] = false;
        }

        // verificare email (format strict)
        if (email == null || email.isEmpty()) {
            valid4[0] = false;
            valid4[1] = false;
        } else {
            valid4[0] = true;
            valid4[1] = email.matches(
                    "^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*@([A-Za-z][A-Za-z0-9]*\\.)+ro$"
            );
        }



        if (valid1[0] &&  valid1[1] &&
                valid2[0] && valid2[1] &&
                valid3[0] && valid3[1] &&
                valid4[0] && valid4[1]) {

            Connection con = DatabaseConnection.getConnection();

            String checkSql1 = "SELECT count(*) FROM elevi WHERE cnp = ?";
            String checkSql2 = "SELECT count(*) FROM elevi WHERE email = ?";
            String sameIdSql1 = "SELECT elev_id FROM elevi WHERE email = ?";
            String sameIdSql2 = "SELECT elev_id FROM elevi WHERE cnp = ?";
            String checkSql3 = "SELECT count(*) FROM profesori WHERE cnp = ?";
            String checkSql4 = "SELECT count(*) FROM profesori WHERE email = ?";
            String sql = "UPDATE elevi SET nume = ?, prenume = ?, cnp = ?, email = ? WHERE elev_id = ?";

            PreparedStatement ps1 = con.prepareStatement(checkSql1);
            PreparedStatement ps2 = con.prepareStatement(checkSql2);
            PreparedStatement ps3 = con.prepareStatement(checkSql3);
            PreparedStatement ps4 = con.prepareStatement(checkSql4);
            ps1.setNString(1, cnp);
            ps2.setNString(1, email);
            ps3.setNString(1, cnp);
            ps4.setNString(1, email);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            ResultSet rs3 = ps3.executeQuery();
            ResultSet rs4 = ps4.executeQuery();

            rs1.next();
            int count1 = rs1.getInt(1);
            rs1.close();
            ps1.close();

            rs2.next();
            int count2 = rs2.getInt(1);
            rs2.close();
            ps2.close();

            rs3.next();
            int count3 = rs3.getInt(1);
            rs3.close();
            ps3.close();

            rs4.next();
            int count4 = rs4.getInt(1);
            rs4.close();
            ps4.close();

            boolean ok = true;

            PreparedStatement psFind1 = con.prepareStatement(sameIdSql1);
            PreparedStatement psFind2 = con.prepareStatement(sameIdSql2);
            psFind1.setNString(1, email);
            psFind2.setNString(1, cnp);
            ResultSet rFind1 = psFind1.executeQuery();
            ResultSet rFind2 = psFind2.executeQuery();
            int ElevId = 0;
            while (rFind1.next()) {
                ElevId = rFind1.getInt(1);
                if (ElevId != elev_id)
                    ok = false;
            }
            while (rFind2.next()) {
                ElevId = rFind2.getInt(1);
                if (ElevId != elev_id)
                    ok = false;
            }

            if (ok)
            {
                PreparedStatement psIntro = con.prepareStatement(sql);

                psIntro.setNString(1, nume);
                psIntro.setNString(2, prenume);
                psIntro.setNString(3, cnp);

                psIntro.setNString(4, email);

                psIntro.setInt(5, elev_id);

                psIntro.executeUpdate();
                psIntro.close();

                messageValue = 1;
                message = "Elev adaugat cu succes :)";
            }
            else
            {
                if (count1 != 0 || count3 != 0)
                {
                    messageValue = 2;
                    message = "Exista deja o persoana cu CNP-ul introdus de tine";
                }
                if (count2 != 0 || count4 != 0)
                {
                    messageValue = 3;
                    message = "Exista deja o persoana cu adresa de e-mail introdusa de tine";
                }

            }
        }
        model.addAttribute("valid10", valid1[0]);
        model.addAttribute("valid11", valid1[1]);
        model.addAttribute("valid20", valid2[0]);
        model.addAttribute("valid21", valid2[1]);
        model.addAttribute("valid30", valid3[0]);
        model.addAttribute("valid31", valid3[1]);
        model.addAttribute("valid40", valid4[0]);
        model.addAttribute("valid41", valid4[1]);
        model.addAttribute("messageValue", messageValue);
        model.addAttribute("message", message);

        if (valid1[0] &&  valid1[1] &&
                valid2[0] && valid2[1] &&
                valid3[0] && valid3[1] &&
                valid4[0] && valid4[1] && messageValue == 1)
        {
            model.addAttribute("nume", "");
            model.addAttribute("prenume", "");
            model.addAttribute("cnp", "");
            model.addAttribute("email", "");
        }
        else
        {
            model.addAttribute("nume", nume);
            model.addAttribute("prenume", prenume);
            model.addAttribute("cnp", cnp);
            model.addAttribute("email", email);
        }

        if (messageValue == 1)
        {

            return "redirect:/eleviIndex?id=" + clasa_id;
        }
        else {
            model.addAttribute("elev_id", elev_id);
            model.addAttribute("clasa_id", clasa_id);
            return "Elev/modify";
        }
    }

    @PostMapping("/eleviSearch")
    public String eleviSearch(@RequestParam("keyword") String keyword,
                              @RequestParam("clasa_id") int clasa_id,
                              Model model) throws SQLException
    {
        keyword = keyword.trim();
        List<Elev> elevi = new ArrayList<>();

        Connection con = DatabaseConnection.getConnection();
        if (keyword.isEmpty())
        {
            String sql = "SELECT elev_id, nume, prenume, cnp, email FROM elevi WHERE clasa_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, clasa_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Elev elev1 = new Elev();

                elev1.setId(rs.getInt("elev_id"));
                elev1.setNume(rs.getString("nume"));
                elev1.setPrenume(rs.getString("prenume"));
                elev1.setCnp(rs.getString("cnp"));
                elev1.setEmail(rs.getString("email"));

                elevi.add(elev1);
            }
            ps.close();
        }
        else
        {
            String sql = "SELECT elev_id, nume, prenume, cnp, email FROM elevi " +
                    "WHERE ISNULL(nume,'') + ' ' + ISNULL(prenume,'') + ' ' + ISNULL(email,'') LIKE ? AND clasa_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setNString(1, "%" + keyword + "%");
            ps.setInt(2, clasa_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Elev elev1 = new Elev();

                elev1.setId(rs.getInt("elev_id"));
                elev1.setNume(rs.getString("nume"));
                elev1.setPrenume(rs.getString("prenume"));
                elev1.setCnp(rs.getString("cnp"));
                elev1.setEmail(rs.getString("email"));

                elevi.add(elev1);
            }
            ps.close();
        }

        model.addAttribute("elevi", elevi);
        model.addAttribute("clasa_id", clasa_id);
        return "Elev/index";

    }

    @PostMapping("/eleviDelete")
    public String eleviDelete(@RequestParam("elev_id") int elev_id,
            @RequestParam("clasa_id") int clasa_id) throws SQLException {

        String sql = "DELETE FROM elevi WHERE elev_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);)
        {
            ps.setInt(1, elev_id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Eroare la stergerea clasei cu id=" + elev_id, e);
        }


        return "redirect:/eleviIndex?id=" + clasa_id;
    }
}
