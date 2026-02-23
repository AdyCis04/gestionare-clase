/** Clasa pentru crearea unei instante de tip clasa de elevi, Clasa
        * @author Cismaru Adrian
* @version 10 ianuarie 2026
        */

        package com.example.gestionare_clase.classes;

public class Clasa {
    private Integer id;
    private String denumire;
    private String profil;
    private String specializare;
    private String anScolar;

    public Clasa() {
        id = 0;
        denumire = "";
        profil = "";
        specializare = "";
        anScolar = "";
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDenumire() {
        return denumire;
    }
    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
    public String getProfil() {
        return profil;
    }
    public void setProfil(String profil) {
        this.profil = profil;
    }
    public String getSpecializare() {
        return specializare;
    }
    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }
    public String getAnScolar() {
        return anScolar;
    }
    public void setAnScolar(String anScolar) {
        this.anScolar = anScolar;
    }

}
