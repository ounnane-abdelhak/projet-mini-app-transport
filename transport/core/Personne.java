package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public abstract class Personne implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String nom;
    protected String prenom;
    protected LocalDate dateNaissance;
    protected boolean handicap;

    public Personne(String nom, String prenom, LocalDate dateNaissance, boolean handicap) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.handicap = handicap;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public boolean isHandicap() {
        return handicap;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
    }

    // New method to calculate age as of a reference date
    public int getAge(LocalDate referenceDate) {
        if (this.dateNaissance == null || referenceDate == null) {
            return 0; // Or throw an exception, or handle as appropriate
        }
        return Period.between(this.dateNaissance, referenceDate).getYears();
    }

    @Override
    public String toString() {
        return prenom + " " + nom + (handicap ? " (Handicapé)" : "");
    }
}


