package transport.core;
import java.io.Serializable;
public class MoyenTransport implements Suspendable, Serializable  {
    private String identifiant;
    private boolean suspendu = false;

    public MoyenTransport(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    @Override
    public void suspendre() {
        this.suspendu = true;
        System.out.println("Moyen de transport " + identifiant + " suspendu.");
    }

    @Override
    public void reactiver() {
        this.suspendu = false;
        System.out.println("Moyen de transport " + identifiant + " réactivé.");
    }

    @Override
    public boolean estSuspendu() {
        return this.suspendu;
    }

    @Override
    public String getEtat() {
        return this.suspendu ? "Suspendu" : "Actif";
    }

    @Override
    public String toString() {
        return "MoyenTransport{" +
               "identifiant='" + identifiant + '\'' +
               ", etat='" + getEtat() + '\'' +
               '}';
    }
}

