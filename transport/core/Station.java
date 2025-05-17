package transport.core;

public class Station implements Suspendable {
    private String nom;
    private String adresse;
    private boolean suspendu = false;

    public Station(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    @Override
    public void suspendre() {
        this.suspendu = true;
        System.out.println("Station " + nom + " suspendue.");
    }

    @Override
    public void reactiver() {
        this.suspendu = false;
        System.out.println("Station " + nom + " réactivée.");
    }

    @Override
    public boolean estSuspendu() {
        return this.suspendu;
    }

    @Override
    public String getEtat() {
        return this.suspendu ? "Suspendue" : "Active";
    }

    @Override
    public String toString() {
        return "Station{" +
               "nom=\'" + nom + '\'' +
               ", adresse=\'" + adresse + '\'' +
               ", etat=\'" + getEtat() + '\'' +
               '}';
    }
}

