package transport.core;

import java.time.LocalDate;

public class Usager extends Personne {

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicap) {
        super(nom, prenom, dateNaissance, handicap);
    }

    @Override
    public String toString() {
        return super.toString() + " (Usager)";
    }
}

