package transport.core;

import java.time.LocalDate;
import java.time.Period;

public abstract class Personne {
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

    public int getAge(LocalDate currentDate) {
        if ((dateNaissance != null) && (currentDate != null)) {
            return Period.between(dateNaissance, currentDate).getYears();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}

