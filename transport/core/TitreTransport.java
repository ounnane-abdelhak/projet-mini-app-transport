package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TitreTransport implements Suspendable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger nextIdTitre = new AtomicInteger(1);

    protected final int idTitre;
    protected LocalDate dateEmission;
    protected LocalDate dateExpiration;
    protected boolean estSuspendu;
    protected double prix; // Price attribute as per PDF

    // Constructor updated to include prix
    public TitreTransport(LocalDate dateEmission, LocalDate dateExpiration, double prix) {
        this.idTitre = nextIdTitre.getAndIncrement();
        this.dateEmission = dateEmission;
        this.dateExpiration = dateExpiration;
        this.prix = prix;
        this.estSuspendu = false;
    }

    // Getters
    public int getIdTitre() { return idTitre; }
    public LocalDate getDateEmission() { return dateEmission; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public double getPrix() { return prix; } // Getter for prix

    // Suspendable implementation
    @Override
    public void suspendre() { this.estSuspendu = true; }
    @Override
    public void reactiver() { this.estSuspendu = false; }
    @Override
    public boolean estSuspendu() { return this.estSuspendu; }
    @Override
    public String getEtat() { return estSuspendu ? "Suspendu" : "Actif"; }

    public abstract boolean valider() throws TitreNonValideException;

    @Override
    public String toString() {
        return "Titre N°" + idTitre +
                ", Emission: " + dateEmission +
                ", Expiration: " + dateExpiration +
                ", Prix: " + String.format("%.2f", prix) + " DA" +
                ", Etat: " + getEtat();
    }
}
