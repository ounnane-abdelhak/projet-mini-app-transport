package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Ticket extends TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double PRIX_TICKET = 50.0; // Fixed price for a ticket

    public Ticket(LocalDate dateEmission) {
        // Ticket is valid only on the date of emission and has a fixed price.
        super(dateEmission, dateEmission, PRIX_TICKET);
    }

    @Override
    public boolean valider() throws TitreNonValideException {
        if (estSuspendu()) { // Unlikely for tickets but good for consistency
            throw new TitreNonValideException("Ticket N°" + getIdTitre() + " est suspendu.");
        }
        if (!LocalDate.now().isEqual(this.dateEmission)) {
            throw new TitreNonValideException("Ticket N°" + getIdTitre() + " est valide uniquement le " + this.dateEmission + ".");
        }
        System.out.println("Ticket N°" + getIdTitre() + " validé avec succès.");
        return true;
    }

    @Override
    public String toString() {
        return "Ticket N°" + getIdTitre() +
                " [Date Emission/Validité: " + dateEmission +
                ", Prix: " + String.format("%.2f", prix) + " DA" + // Use inherited prix
                ", Etat: " + getEtat() + "]";
    }
}
