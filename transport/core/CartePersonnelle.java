package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class CartePersonnelle extends TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;
    private Personne titulaire;
    private CarteType typeCarte;
    private Methodpay selectedmethode;
    // The 'prix' field is now inherited from TitreTransport and set via super()

    private static final double PRIX_BASE_CARTE = 5000.0; // Example base price for a card

    public CartePersonnelle(LocalDate dateEmission, Personne titulaire, CarteType typeCarte ,Methodpay selectedmethode) throws ReductionImpossibleException {
        // Calculate price first, then call super constructor
        super(dateEmission, dateEmission.plusYears(1), calculateFinalPriceHelper(dateEmission, titulaire, typeCarte));
        this.titulaire = titulaire;
        this.typeCarte = typeCarte;
        this.selectedmethode = selectedmethode;
    }

    // Static helper method to calculate price, so it can be called before super()
    private static double calculateFinalPriceHelper(LocalDate dateEmissionCarte, Personne titulaireCarte, CarteType typeCarteAssigned) throws ReductionImpossibleException {
        if (titulaireCarte == null) {
            throw new ReductionImpossibleException("Titulaire de la carte non défini.");
        }

        int age = titulaireCarte.getAge(dateEmissionCarte); // Assumes Personne.getAge(referenceDate) exists
        double reduction = 0.0;

        switch (typeCarteAssigned) {
            case JUNIOR:
                if (age < 0) throw new ReductionImpossibleException("Date de naissance invalide pour le titulaire.");
                if (age <= 25) {
                    reduction = 0.20; // 20% reduction
                } else {
                    throw new ReductionImpossibleException("Le titulaire ne qualifie pas pour la carte JUNIOR (age > 25).");
                }
                break;
            case SENIOR:
                if (age < 0) throw new ReductionImpossibleException("Date de naissance invalide pour le titulaire.");
                if (age >= 60) {
                    reduction = 0.30; // 30% reduction
                } else {
                    throw new ReductionImpossibleException("Le titulaire ne qualifie pas pour la carte SENIOR (age < 60).");
                }
                break;
            case SOLIDARITE:
                if (titulaireCarte.isHandicap()) {
                    reduction = 0.50; // 50% reduction
                } else {
                    throw new ReductionImpossibleException("Le titulaire ne qualifie pas pour la carte SOLIDARITE (non handicapé).");
                }
                break;
            case PARTENAIRE:
                reduction = 0.10; // 10% reduction
                break;
            default:
                throw new ReductionImpossibleException("Type de carte inconnu.");
        }
        return PRIX_BASE_CARTE * (1 - reduction);
    }

    @Override
    public boolean valider() throws TitreNonValideException {
        if (estSuspendu()) {
            throw new TitreNonValideException("Carte N°" + getIdTitre() + " est suspendue.");
        }
        if (LocalDate.now().isAfter(this.dateExpiration)) {
            throw new TitreNonValideException("Carte N°" + getIdTitre() + " a expiré le " + this.dateExpiration + ".");
        }
        System.out.println("Carte N°" + getIdTitre() + " validée avec succès.");
        return true;
    }

    // Getters
    public Personne getTitulaire() { return titulaire; }
    public CarteType getTypeCarte() { return typeCarte; }
    // getPrix() is inherited from TitreTransport

    @Override
    public String toString() {
        return "CartePersonnelle N°" + getIdTitre() +
                " [Titulaire: " + (titulaire != null ? titulaire.getPrenom() + " " + titulaire.getNom() : "N/A") +
                ", Type: " + typeCarte +
                ", Payment Method: "+selectedmethode+
                ", Emission: " + dateEmission +
                ", Expiration: " + dateExpiration +
                ", Prix: " + String.format("%.2f", prix) + " DA" +
                ", Etat: " + getEtat() + "]";
    }
}
