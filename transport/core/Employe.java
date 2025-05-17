package transport.core;

import java.io.Serializable; // <<< ADD THIS IMPORT (though inherited, good for clarity)
import java.time.LocalDate;

public class Employe extends Personne implements Serializable { // <<< ADD "implements Serializable" (Personne is already Serializable)

    private static final long serialVersionUID = 1L; // Good practice
    private String matricule;
    private TypeFonction fonction;

    public Employe(String nom, String prenom, LocalDate dateNaissance, boolean handicap, String matricule, TypeFonction fonction) {
        super(nom, prenom, dateNaissance, handicap);
        this.matricule = matricule;
        this.fonction = fonction;
    }

    // Getters
    public String getMatricule() {
        return matricule;
    }

    public TypeFonction getFonction() {
        return fonction;
    }

    // Setters
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setFonction(TypeFonction fonction) {
        this.fonction = fonction;
    }

    @Override
    public String toString() {
        return super.toString() + " [Employe - Matricule: " + matricule + ", Fonction: " + fonction + "]";
    }
}


