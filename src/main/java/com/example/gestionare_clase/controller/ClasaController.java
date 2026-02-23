/** Clasa controller pt fisierele html din folderul Clasa
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase.controller;

import com.example.gestionare_clase.DatabaseConnection;
import com.example.gestionare_clase.Functions;
import com.example.gestionare_clase.classes.Clasa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClasaController {

    @GetMapping("/claseIndex")
    public String claseIndex(Model model) throws SQLException {

        List<Clasa> clase = new ArrayList<>();

        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM clase");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Clasa clasa1 = new Clasa();
            clasa1.setId(rs.getInt("clasa_id"));
            clasa1.setDenumire(rs.getString("denumire"));
            clasa1.setProfil(rs.getString("profil"));
            clasa1.setSpecializare(rs.getString("specializare"));
            clasa1.setAnScolar(rs.getString("an_scolar"));
            clase.add(clasa1);
        }
        model.addAttribute("clase", clase);
        ps.close();

        return "Clasa/index";
    }

    @PostMapping("/claseDelete")
    public String claseDelete(@RequestParam("id") Integer clasa_id) throws SQLException {

        String sql = "EXEC sp_DeleteClasa @clasa_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);)
        {
            ps.setInt(1, clasa_id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Eroare la stergerea clasei cu id=" + clasa_id, e);
        }


        return "redirect:/claseIndex";
    }

    @GetMapping("/claseCreate")
    public String claseCreate(Model model)
    {
        model.addAttribute("messageValue", -1); // sau false

        model.addAttribute("den", "");
        model.addAttribute("spe", "");
        model.addAttribute("an", "");
        return "Clasa/create";
    }

    @PostMapping("/claseSearch")
    public String claseSearch(@RequestParam("keyword") String keyword, Model model) throws SQLException
    {
        if (!keyword.isEmpty() && keyword != null)
            keyword = keyword.trim();

        List<Clasa> clase = new ArrayList<>();

        Connection con = DatabaseConnection.getConnection();
        if (keyword.isEmpty())
        {
            String sql = "SELECT clasa_id, denumire, profil, specializare, an_scolar FROM clase";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Clasa clasa1 = new Clasa();
                clasa1.setId(rs.getInt("clasa_id"));
                clasa1.setDenumire(rs.getString("denumire"));
                clasa1.setProfil(rs.getString("profil"));
                clasa1.setSpecializare(rs.getString("specializare"));
                clasa1.setAnScolar(rs.getString("an_scolar"));
                clase.add(clasa1);
            }
            ps.close();
        }
        else
        {
            String sql = "SELECT clasa_id, denumire, profil, specializare, an_scolar " +
                    "FROM clase WHERE ISNULL(denumire,'') + ' ' + ISNULL(profil,'') + ' ' + ISNULL(specializare,'') + ' ' + ISNULL(an_scolar,'') LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setNString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Clasa clasa1 = new Clasa();
                clasa1.setId(rs.getInt("clasa_id"));
                clasa1.setDenumire(rs.getString("denumire"));
                clasa1.setProfil(rs.getString("profil"));
                clasa1.setSpecializare(rs.getString("specializare"));
                clasa1.setAnScolar(rs.getString("an_scolar"));
                clase.add(clasa1);
            }
            ps.close();
        }

        model.addAttribute("clase", clase);

        return "Clasa/index";
        //return "redirect:/claseIndex";
    }

    @PostMapping("/claseCreatePost")
    public String claseCreatePost(
            @RequestParam("denumire") String denumire,
            @RequestParam("specializare") String specializare,
            @RequestParam("anScolar") String anScolar,
            Model model) throws SQLException
    {
        boolean[] valid1 = new boolean[2];
        boolean[] valid2 = new boolean[2];
        boolean[] valid3 = new boolean[2];
        int messageValue = -1;
        String message = "";

        denumire = denumire.trim();
        specializare = specializare.trim();
        anScolar = anScolar.trim();

        //verificare denumire
        if (denumire.isEmpty() || denumire == null)
        {
            valid1[0] = false;
            valid1[1] = false;
        }
        else
        {
            valid1[0] = true;
            int index = denumire.indexOf(' ');
            while (index >= 0)
            {
                denumire = denumire.substring(index + 1).trim();
                index = denumire.indexOf(' ');
            }

            if (denumire.charAt(0) == '9')
            {
                if ('a' <= denumire.charAt(1) && denumire.charAt(1) <= 'z' || 'A' <= denumire.charAt(1) && denumire.charAt(1) <= 'Z')
                {
                    if (denumire.length() == 2)
                        valid1[1] = true;
                    else
                        valid1[1] = false;
                }
                else
                    valid1[1] = false;
            }
            else{
                if (denumire.charAt(0) == '1')
                {
                    if ('0' <= denumire.charAt(1) && denumire.charAt(1) < '3')
                    {
                        if ('a' <= denumire.charAt(2) && denumire.charAt(2) <= 'z' || 'A' <= denumire.charAt(2) && denumire.charAt(2) <= 'Z')
                        {
                            if (denumire.length() == 3)
                                valid1[1] = true;
                            else
                                valid1[1] = false;
                        }
                        else
                        {
                            valid1[1] = false;
                        }
                    }
                    else
                        valid1[1] = false;
                }
                else
                    valid1[1] = false;
            }
        }


        //verificare specializare
        if (specializare.isEmpty() || specializare == null)
        {
            valid2[0] = false;
            valid2[1] = false;
        }
        else
        {
            valid2[0] = true;
            if (((specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Informatica") == 0 ||
                    specializare.compareToIgnoreCase("Matematica-Informatica") == 0 ||
                    specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Limba Engleza") == 0 ||
                    specializare.compareToIgnoreCase("Stiinte ale naturii") == 0) ) ||
                    ((specializare.compareToIgnoreCase("Stiinte sociale") == 0 ||
                            specializare.compareToIgnoreCase("Stiinte sociale, intensiv Limba Engleza") == 0 ||
                            specializare.compareToIgnoreCase("Filologie") == 0)))
                valid2[1] = true;
            else
                valid2[1] = false;
        }

        //verificare an scolar
        if (anScolar.isEmpty() || anScolar == null)
        {
            valid3[0] = false;
            valid3[1] = false;
        }
        else
        {
            valid3[0] = true;

            if (anScolar.indexOf('/') == anScolar.lastIndexOf('/'))
            {
                String[] str = new String[2];
                str = anScolar.split("/", -1);

                if (Functions.isInteger(str[0]) &&  Functions.isInteger(str[1]))
                {
                    int nr1, nr2;
                    nr1 = Integer.parseInt(str[0]);
                    nr2 = Integer.parseInt(str[1]);
                    valid3[1] = nr1 + 1 == nr2 && nr1 > 2019;
                }
                else
                    valid3[1] = false;
            }
            else
                valid3[1] = false;
        }

        if (valid1[0] &&  valid1[1] && valid2[0] && valid2[1] &&  valid3[0] && valid3[1])
        {
            Connection con = DatabaseConnection.getConnection();

            String checkSql = "SELECT count(*) FROM clase WHERE denumire = ? AND an_scolar = ?";
            String sql = "INSERT INTO clase(denumire, profil ,specializare, an_scolar) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(checkSql);
            ps.setNString(1, denumire);
            ps.setNString(2, anScolar);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            ps.close();

            if (count == 0)
            {
                PreparedStatement ps1 = con.prepareStatement(sql);

                ps1.setNString(1, denumire.toUpperCase());

                if (specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Informatica") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica, intensiv Informatica");
                }
                if (specializare.compareToIgnoreCase("Matematica-Informatica") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica");
                }
                if (specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Limba engleza") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica, intensiv Limba engleza");
                }
                if (specializare.compareToIgnoreCase("Stiinte ale naturii") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Stiinte ale naturii");
                }
                if (specializare.compareToIgnoreCase("Stiinte sociale") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Stiinte sociale");
                }
                if (specializare.compareToIgnoreCase("Stiinte sociale, intensiv Limba engleza") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Stiinte sociale, intensiv Limba engleza");
                }
                if (specializare.compareToIgnoreCase("Filologie") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Filologie");
                }

                ps1.setNString(4, anScolar);

                ps1.executeUpdate();
                ps1.close();

                messageValue = 1;
                message = "Clasa creata cu succes :)";
            }
            else
            {
                messageValue = 0;
                message = "Exista deja o clasa de elevi cu denumirea si anul scolar introduse de tine";
            }
        }
        model.addAttribute("valid10", valid1[0]);
        model.addAttribute("valid11", valid1[1]);
        model.addAttribute("valid20", valid2[0]);
        model.addAttribute("valid21", valid2[1]);
        model.addAttribute("valid30", valid3[0]);
        model.addAttribute("valid31", valid3[1]);
        model.addAttribute("messageValue", messageValue);
        model.addAttribute("message", message);

        if (valid1[0] && valid1[1] && valid2[0] && valid2[1] && valid3[0] && valid3[1] && messageValue == 1)
        {
            model.addAttribute("den", "");
            model.addAttribute("spe", "");
            model.addAttribute("an", "");
        }
        else
        {
            model.addAttribute("den", denumire);
            model.addAttribute("spe", specializare);
            model.addAttribute("an", anScolar);
        }

        return "Clasa/create";
    }

    @GetMapping("/claseModify")
    public String claseModify(
            @RequestParam("id") Integer clasa_id,
            Model model) throws SQLException
    {

        String sql = "SELECT clasa_id, denumire, specializare, an_scolar FROM clase WHERE clasa_id = ?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, clasa_id);
        ResultSet rs = ps.executeQuery();
        Clasa clasa1 = new Clasa();
        while (rs.next()) {

            clasa1.setId(rs.getInt("clasa_id"));
            clasa1.setDenumire(rs.getString("denumire"));
            clasa1.setSpecializare(rs.getString("specializare"));
            clasa1.setAnScolar(rs.getString("an_scolar"));
        }
        ps.close();

        model.addAttribute("id", clasa1.getId());
        model.addAttribute("den", clasa1.getDenumire());
        model.addAttribute("spe", clasa1.getSpecializare());
        model.addAttribute("an", clasa1.getAnScolar());

        return "Clasa/modify";
    }

    @GetMapping("/claseModifyPost")
    public String claseModifyPost(
            @RequestParam("id") int id,
            @RequestParam("denumire") String denumire,
            @RequestParam("specializare") String specializare,
            @RequestParam("anScolar") String anScolar,
            Model model) throws SQLException
    {
        boolean[] valid1 = new boolean[2];
        boolean[] valid2 = new boolean[2];
        boolean[] valid3 = new boolean[2];
        int messageValue = -1;
        String message = "";

        denumire = denumire.trim();
        specializare = specializare.trim();
        anScolar = anScolar.trim();

        //verificare denumire
        if (denumire.isEmpty() || denumire == null)
        {
            valid1[0] = false;
            valid1[1] = false;
        }
        else
        {
            valid1[0] = true;
            int index = denumire.indexOf(' ');
            while (index >= 0)
            {
                denumire = denumire.substring(index + 1).trim();
                index = denumire.indexOf(' ');
            }

            if (denumire.charAt(0) == '9')
            {
                if ('a' <= denumire.charAt(1) && denumire.charAt(1) <= 'z' || 'A' <= denumire.charAt(1) && denumire.charAt(1) <= 'Z')
                {
                    if (denumire.length() == 2)
                        valid1[1] = true;
                    else
                        valid1[1] = false;
                }
                else
                    valid1[1] = false;
            }
            else{
                if (denumire.charAt(0) == '1')
                {
                    if ('0' <= denumire.charAt(1) && denumire.charAt(1) < '3')
                    {
                        if ('a' <= denumire.charAt(2) && denumire.charAt(2) <= 'z' || 'A' <= denumire.charAt(2) && denumire.charAt(2) <= 'Z')
                        {
                            if (denumire.length() == 3)
                                valid1[1] = true;
                            else
                                valid1[1] = false;
                        }
                        else
                        {
                            valid1[1] = false;
                        }
                    }
                    else
                        valid1[1] = false;
                }
                else
                    valid1[1] = false;
            }
        }

        //verificare specializare
        if (specializare.isEmpty() || specializare == null)
        {
            valid2[0] = false;
            valid2[1] = false;
        }
        else
        {
            valid2[0] = true;
            if (((specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Informatica") == 0 ||
                    specializare.compareToIgnoreCase("Matematica-Informatica") == 0 ||
                    specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Limba Engleza") == 0 ||
                    specializare.compareToIgnoreCase("Stiinte ale naturii") == 0) ) ||
                    ((specializare.compareToIgnoreCase("Stiinte sociale") == 0 ||
                            specializare.compareToIgnoreCase("Stiinte sociale, intensiv Limba Engleza") == 0 ||
                            specializare.compareToIgnoreCase("Filologie") == 0)))
                valid2[1] = true;
            else
                valid2[1] = false;
        }

        //verificare an scolar
        if (anScolar.isEmpty() || anScolar == null)
        {
            valid3[0] = false;
            valid3[1] = false;
        }
        else
        {
            valid3[0] = true;

            if (anScolar.indexOf('/') == anScolar.lastIndexOf('/'))
            {
                String[] str = new String[2];
                str = anScolar.split("/", -1);

                if (Functions.isInteger(str[0]) &&  Functions.isInteger(str[1]))
                {
                    int nr1, nr2;
                    nr1 = Integer.parseInt(str[0]);
                    nr2 = Integer.parseInt(str[1]);
                    valid3[1] = nr1 + 1 == nr2 && nr1 > 2019;
                }
                else
                    valid3[1] = false;
            }
            else
                valid3[1] = false;
        }

        if (valid1[0] && valid1[1] && valid2[0] && valid2[1] && valid3[0] && valid3[1])
        {
            Connection con = DatabaseConnection.getConnection();

            String checkSql = "SELECT count(*) FROM clase WHERE denumire = ? AND an_scolar = ?";
            String sameIdSql = "SELECT clasa_id FROM clase WHERE denumire = ? AND an_scolar = ?";
            String sql = "UPDATE clase SET denumire = ?, profil = ?, specializare = ?, an_scolar = ? WHERE clasa_id = ?";

            PreparedStatement ps = con.prepareStatement(checkSql);
            ps.setNString(1, denumire);
            ps.setNString(2, anScolar);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            ps.close();

            PreparedStatement pss = con.prepareStatement(sameIdSql);
            pss.setNString(1, denumire);
            pss.setNString(2, anScolar);
            ResultSet rss = pss.executeQuery();
            int clasaId = 0;
            while (rss.next())
            {
                clasaId = rss.getInt(1);
            }

            boolean ok = count == 0 || (count > 0 && clasaId == id);

            if (ok)
            {
                PreparedStatement ps1 = con.prepareStatement(sql);

                ps1.setNString(1, denumire.toUpperCase());

                if (specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Informatica") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica, intensiv Informatica");
                }
                if (specializare.compareToIgnoreCase("Matematica-Informatica") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica");
                }
                if (specializare.compareToIgnoreCase("Matematica-Informatica, intensiv Limba engleza") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Matematica-Informatica, intensiv Limba engleza");
                }
                if (specializare.compareToIgnoreCase("Stiinte ale naturii") == 0)
                {
                    ps1.setNString(2, "Real");
                    ps1.setNString(3, "Stiinte ale naturii");
                }
                if (specializare.compareToIgnoreCase("Stiinte sociale") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Stiinte sociale");
                }
                if (specializare.compareToIgnoreCase("Stiinte sociale, intensiv Limba engleza") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Stiinte sociale, intensiv Limba engleza");
                }
                if (specializare.compareToIgnoreCase("Filologie") == 0)
                {
                    ps1.setNString(2, "Uman");
                    ps1.setNString(3, "Filologie");
                }

                ps1.setNString(4, anScolar);
                ps1.setInt(5, id);

                ps1.executeUpdate();
                ps1.close();

                messageValue = 1;
                message = "Clasa modificata cu succes :)";
            }
            else
            {
                messageValue = 0;
                message = "Exista deja o clasa de elevi cu denumirea si anul scolar introduse de tine";
            }
        }
        model.addAttribute("valid10", valid1[0]);
        model.addAttribute("valid11", valid1[1]);
        model.addAttribute("valid20", valid2[0]);
        model.addAttribute("valid21", valid2[1]);
        model.addAttribute("valid30", valid3[0]);
        model.addAttribute("valid31", valid3[1]);
        model.addAttribute("messageValue", messageValue);
        model.addAttribute("message", message);

        if (valid1[0] && valid1[1] && valid2[0] && valid2[1] && valid3[0] && valid3[1] && messageValue == 1)
        {
            model.addAttribute("den", "");
            model.addAttribute("spe", "");
            model.addAttribute("an", "");
        }
        else
        {
            model.addAttribute("id", id);
            model.addAttribute("den", denumire);
            model.addAttribute("spe", specializare);
            model.addAttribute("an", anScolar);
        }

        if (messageValue != 0)
        {

            return "redirect:/claseIndex";
        }
        else {

            return "Clasa/modify";
        }
    }

}
