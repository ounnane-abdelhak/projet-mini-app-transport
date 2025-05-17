package transport.core;

import java.time.LocalDate;

public abstract class TitreTransport {
    private static int nextId = 1;
    protected int idTitre;
    protected LocalDate dateEmission;
    protected double prix;

    public TitreTransport(LocalDate dateEmission, double prix) {
        this.idTitre = nextId++;
        this.dateEmission = dateEmission;
        this.prix = prix;
    }

    public int getIdTitre() {
        return idTitre;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public abstract boolean valider() throws TitreNonValideException;

    public void appliquerReduction(double pourcentage) throws ReductionImpossibleException {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new ReductionImpossibleException("Le pourcentage de réduction doit être entre 0 et 100.");
        }
        this.prix -= this.prix * (pourcentage / 100.0);
    }

    @Override
    public String toString() {
        return "Titre N°" + idTitre + ", emis le " + dateEmission + ", prix: " + String.format("%.2f", prix) + " DA";
    }
}

