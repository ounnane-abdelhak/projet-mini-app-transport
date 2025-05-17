package transport.core;

import java.time.LocalDate;

public class Employe extends Personne {
    protected String matricule;
    protected TypeFonction typeFonction;

    public Employe(String nom, String prenom, LocalDate dateNaissance, boolean handicap, String matricule, TypeFonction typeFonction) {
        super(nom, prenom, dateNaissance, handicap);
        this.matricule = matricule;
        this.typeFonction = typeFonction;
    }

    public String getMatricule() {
        return matricule;
    }

    public TypeFonction getTypeFonction() {
        return typeFonction;
    }

    @Override
    public String toString() {
        return super.toString() + " (Matricule: " + matricule + ", Fonction: " + typeFonction + ")";
    }
}

