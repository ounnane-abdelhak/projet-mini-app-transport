package transport.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceReclamation {
    private List<Reclamation> reclamations;

    public ServiceReclamation() {
        this.reclamations = new ArrayList<>();
    }

    public void addReclamation(Reclamation reclamation) {
        if (reclamation != null) {
            this.reclamations.add(reclamation);
            System.out.println("Réclamation N°" + reclamation.getNumero() + " ajoutée.");

            // Bonus: Display a suspension message for a station or a transport method if more than 3 complaints are recorded.
            Suspendable entiteConcernee = reclamation.getEntiteConcernee();
            if (entiteConcernee != null) {
                long count = countReclamationsForEntity(entiteConcernee);
                if (count > 3 && !entiteConcernee.estSuspendu()) {
                    entiteConcernee.suspendre();
                    System.out.println("ALERTE: L'entité " + entiteConcernee.toString() + " a maintenant " + count + " réclamations et a été suspendue.");
                }
            }
        }
    }

    public List<Reclamation> getAllReclamations() {
        return new ArrayList<>(reclamations); // Return a copy to prevent external modification
    }

    public List<Reclamation> getReclamationsByEntity(Suspendable entity) {
        return reclamations.stream()
                .filter(r -> r.getEntiteConcernee() != null && r.getEntiteConcernee().equals(entity))
                .collect(Collectors.toList());
    }

    public long countReclamationsForEntity(Suspendable entity) {
        return reclamations.stream()
                .filter(r -> r.getEntiteConcernee() != null && r.getEntiteConcernee().equals(entity))
                .count();
    }

    public void displayAllReclamations() {
        if (reclamations.isEmpty()) {
            System.out.println("Aucune réclamation enregistrée.");
            return;
        }
        System.out.println("\n--- Liste des Réclamations ---");
        for (Reclamation r : reclamations) {
            System.out.println(r.toString());
        }
        System.out.println("-----------------------------");
    }
}

