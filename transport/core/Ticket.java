package transport.core;

import java.time.LocalDate;

public class Ticket extends TitreTransport {
    // No specific attributes mentioned for Ticket beyond what TitreTransport provides, other than validation logic.
    // The PDF mentions "a ticket (50 DA, valid only on the purchase date)".
    // The price and purchase date (dateEmission) are handled by TitreTransport.

    public Ticket(LocalDate dateAchat) {
        super(dateAchat, 50.0); // Price is fixed at 50 DA as per requirements
    }

    @Override
    public boolean valider() throws TitreNonValideException {
        // Valid only on the purchase date (dateEmission)
        if (!LocalDate.now().isEqual(this.dateEmission)) {
            throw new TitreNonValideException("Ticket N°" + getIdTitre() + " est uniquement valide le jour de l'achat (" + this.dateEmission + ").");
        }
        // Additional validation logic can be added here if needed (e.g., if it's already been used)
        // For now, we assume it's valid if the date matches.
        System.out.println("Ticket N°" + getIdTitre() + " validé avec succès.");
        return true;
    }

    @Override
    public String toString() {
        return "Ticket " + super.toString();
    }
}

