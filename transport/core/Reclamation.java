package transport.core;

import java.time.LocalDate;

public class Reclamation {
    private static int nextNumero = 1;
    private int numero;
    private LocalDate date;
    private TypeReclamation type;
    private String description;
    private Personne personne; // The person reporting the complaint
    private Suspendable entiteConcernee; // Can be MoyenTransport or Station

    public Reclamation(LocalDate date, TypeReclamation type, String description, Personne personne, Suspendable entiteConcernee) {
        this.numero = nextNumero++;
        this.date = date;
        this.type = type;
        this.description = description;
        this.personne = personne;
        this.entiteConcernee = entiteConcernee;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getDate() {
        return date;
    }

    public TypeReclamation getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Personne getPersonne() {
        return personne;
    }

    public Suspendable getEntiteConcernee() {
        return entiteConcernee;
    }

    @Override
    public String toString() {
        String entiteStr = "N/A";
        if (entiteConcernee instanceof MoyenTransport) {
            entiteStr = "MoyenTransport: " + ((MoyenTransport) entiteConcernee).getIdentifiant();
        } else if (entiteConcernee instanceof Station) {
            entiteStr = "Station: " + ((Station) entiteConcernee).getNom();
        }

        return "Reclamation N°" + numero +
               ", Date: " + date +
               ", Type: " + type +
               ", Description: '" + description + "'" +
               ", Rapporteur: " + personne.getPrenom() + " " + personne.getNom() +
               ", Entité concernée: " + entiteStr;
    }
}

