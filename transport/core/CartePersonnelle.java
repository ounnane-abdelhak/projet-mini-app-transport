package transport.core;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CartePersonnelle extends TitreTransport {
    private LocalDate dateExpiration;
    private CarteType typeCarte;
    private Personne titulaire; // To access age for JUNIOR/SENIOR discounts

    public CartePersonnelle(LocalDate dateEmission, Personne titulaire, CarteType typeCarte) {
        super(dateEmission, 0); // Initial price set to 0, will be calculated
        this.titulaire = titulaire;
        this.typeCarte = typeCarte;
        this.dateExpiration = calculateDateExpiration(dateEmission, typeCarte);
        this.setPrix(calculatePrix()); // Calculate and set the price
    }

    private LocalDate calculateDateExpiration(LocalDate emissionDate, CarteType type) {
        switch (type) {
            case JUNIOR:
            case SENIOR:
            case PARTENAIRE:
                return emissionDate.plusYears(1);
            case SOLIDARITE:
                return emissionDate.plusMonths(3); // Example: Solidarity card valid for 3 months
            default:
                return emissionDate.plusYears(1); // Default to 1 year
        }
    }

    private double calculatePrix() {
        double basePrice = 1000; // Example base price for a yearly card
        double discount = 0;

        int age = titulaire.getAge(LocalDate.now());

        switch (this.typeCarte) {
            case JUNIOR:
                if (age < 18) discount = 0.50; // 50% discount for juniors
                else System.out.println("Avertissement: Profil Junior appliqué à une personne de 18 ans ou plus.");
                break;
            case SENIOR:
                if (age >= 60) discount = 0.40; // 40% discount for seniors
                else System.out.println("Avertissement: Profil Senior appliqué à une personne de moins de 60 ans.");
                break;
            case SOLIDARITE:
                discount = 0.75; // 75% discount for solidarity
                basePrice = 250; // Solidarity card might have a different base price for 3 months
                break;
            case PARTENAIRE:
                discount = 0.20; // 20% discount for partners
                break;
        }
        return basePrice * (1 - discount);
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public CarteType getTypeCarte() {
        return typeCarte;
    }

    public Personne getTitulaire() {
        return titulaire;
    }

    @Override
    public boolean valider() throws TitreNonValideException {
        if (LocalDate.now().isAfter(this.dateExpiration)) {
            throw new TitreNonValideException("Carte Personnelle N°" + getIdTitre() + " a expiré le " + this.dateExpiration + ".");
        }
        System.out.println("Carte Personnelle N°" + getIdTitre() + " validée avec succès. Valide jusqu'au " + this.dateExpiration + ".");
        return true;
    }

    @Override
    public void appliquerReduction(double pourcentage) throws ReductionImpossibleException {
        // For personal cards, discounts are usually profile-based, not arbitrary percentage reductions.
        // However, if a general reduction needs to be applied on top, it can be implemented.
        // For now, we consider the price fixed after initial calculation based on profile.
        throw new ReductionImpossibleException("Les réductions sur les cartes personnelles sont appliquées lors de la création en fonction du profil.");
    }

    @Override
    public String toString() {
        return "Carte Personnelle " + super.toString() + ", Type: " + typeCarte + ", Titulaire: " + titulaire.getPrenom() + " " + titulaire.getNom() + ", Expire le: " + dateExpiration;
    }
}

