package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Reclamation implements Serializable { // Ensure it implements Serializable

    private static final long serialVersionUID = 1L; // Good practice for Serializable classes
    private static final AtomicInteger count = new AtomicInteger(0); // For unique ID generation
    private final int numero;
    private LocalDate dateReclamation;
    private TypeReclamation type;
    private String description;
    private Personne demandeur; // The person making the complaint
    private Suspendable concerne; // The entity concerned by the complaint (Station or MoyenTransport), can be null

    public Reclamation(LocalDate dateReclamation, TypeReclamation type, String description, Personne demandeur, Suspendable concerne) {
        this.numero = count.incrementAndGet();
        this.dateReclamation = dateReclamation;
        this.type = type;
        this.description = description;
        this.demandeur = demandeur;
        this.concerne = concerne;
    }

    // --- Getters ---
    public int getNumero() {
        return numero;
    }

    public LocalDate getDateReclamation() {
        return dateReclamation;
    }

    public TypeReclamation getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Personne getDemandeur() {
        return demandeur;
    }

    // **This is the missing getter method**
    public Suspendable getConcerne() {
        return concerne;
    }

    // --- Setters (if needed, but typically complaints are immutable once created) ---
    public void setDateReclamation(LocalDate dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public void setType(TypeReclamation type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDemandeur(Personne demandeur) {
        this.demandeur = demandeur;
    }

    public void setConcerne(Suspendable concerne) {
        this.concerne = concerne;
    }

    @Override
    public String toString() {
        return "Reclamation N°" + numero +
                " [Date: " + dateReclamation +
                ", Type: " + type +
                ", Demandeur: " + (demandeur != null ? demandeur.getNom() + " " + demandeur.getPrenom() : "N/A") +
                ", Concerne: " + (concerne != null ? concerne.toString() : "N/A") +
                ", Description: '" + description + "']";
    }
}
