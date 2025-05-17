package transport.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class ServiceReclamation implements Serializable { // Make Serializable

    private static final long serialVersionUID = 1L; // Good practice
    private List<Reclamation> reclamations;
    private static final String RECLAMATIONS_FILE = "reclamations.dat";

    public ServiceReclamation() {
        this.reclamations = new ArrayList<>();
    }

    public void addReclamation(Reclamation reclamation) {
        if (reclamation != null) {
            this.reclamations.add(reclamation);
            // Check for suspension logic if needed here
            checkAndApplySuspension(reclamation.getConcerne());
        }
    }

    public List<Reclamation> getAllReclamations() {
        return new ArrayList<>(this.reclamations);
    }

    @SuppressWarnings("unchecked")
    public void loadReclamations() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RECLAMATIONS_FILE))) {
            this.reclamations = (List<Reclamation>) ois.readObject();
            System.out.println("Reclamations loaded successfully from " + RECLAMATIONS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Reclamations file not found, starting with an empty list.");
            this.reclamations = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reclamations: " + e.getMessage());
            e.printStackTrace();
            this.reclamations = new ArrayList<>(); // Start fresh on error
        }
    }

    public void saveReclamations() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RECLAMATIONS_FILE))) {
            oos.writeObject(this.reclamations);
            System.out.println("Reclamations saved successfully to " + RECLAMATIONS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving reclamations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkAndApplySuspension(Suspendable entity) {
        if (entity == null) return;

        long count = reclamations.stream()
                .filter(r -> entity.equals(r.getConcerne()))
                .count();

        if (count >= 3 && !entity.estSuspendu()) {
            entity.suspendre();
            System.out.println("Entity " + entity.toString() + " has been suspended due to multiple complaints.");
            // You might want to notify the UI or log this more formally
        }
    }
}


