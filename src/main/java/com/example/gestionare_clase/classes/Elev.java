/** Clasa pentru crearea unei instante de tip elev
 * @author Cismaru Adrian
 * @version 10 ianuarie 2026
 */

package com.example.gestionare_clase.classes;
import java.time.LocalDate;
import java.util.Date;

public class Elev {
    private Integer id;
    private Integer clasaId;
    private String nume;
    private String prenume;
    private String cnp;
    private String email;

    public Elev()
    {
        id = 0;
        clasaId = 0;
        nume = "";
        prenume = "";
        cnp = "";
        email = "";
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getClasaId() { return clasaId; }
    public void setClasaId(Integer clasa_id) {
        this.clasaId = clasa_id;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public String getCnp() {
        return cnp;
    }
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
