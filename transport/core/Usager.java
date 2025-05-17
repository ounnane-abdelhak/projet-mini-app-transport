package transport.core;

import java.io.Serializable; // <<< ADD THIS IMPORT (though inherited, good for clarity)
import java.time.LocalDate;

public class Usager extends Personne implements Serializable { // <<< ADD "implements Serializable" (Personne is already Serializable)

    private static final long serialVersionUID = 1L; // Good practice
    // Usager specific fields, if any, would go here.
    // For now, it seems to primarily inherit from Personne.

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicap) {
        super(nom, prenom, dateNaissance, handicap);
    }

    @Override
    public String toString() {
        return super.toString() + " [Usager]";
    }
}
