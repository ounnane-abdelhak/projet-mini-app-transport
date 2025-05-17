package transport.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Placeholder for DataManager. Full implementation will depend on specific UI interactions and data flow.
public class DataManager {

    private static final String USERS_FILE = "users.dat";
    private static final String FARES_FILE = "fares.dat";
    private static final String COMPLAINTS_FILE = "complaints.dat";

    // Example: Storing lists in memory. These would be managed by respective services or this manager.
    private List<Personne> personnes; // Includes Usager and Employe
    private List<TitreTransport> titresVendus;
    private List<Reclamation> reclamations;

    public DataManager() {
        this.personnes = new ArrayList<>();
        this.titresVendus = new ArrayList<>();
        this.reclamations = new ArrayList<>();
        // Optionally load data on initialization
        // loadAllData(); 
    }

    // --- Personne Management ---
    public void addPersonne(Personne personne) {
        this.personnes.add(personne);
    }

    public List<Personne> getPersonnes() {
        return new ArrayList<>(personnes);
    }

    // --- TitreTransport Management ---
    public void addTitreVendu(TitreTransport titre) {
        this.titresVendus.add(titre);
    }

    public List<TitreTransport> getTitresVendus() {
        // Requirement: Display the list of sold fare media in descending order of purchase date (most recent first).
        // This sorting should ideally be done when retrieving for display.
        return new ArrayList<>(titresVendus);
    }

    // --- Reclamation Management (already handled by ServiceReclamation, integrate or delegate) ---
    // For now, ServiceReclamation handles its own list. DataManager could persist ServiceReclamation's data.

    // --- Data Persistence Methods (Example using Object Serialization) ---

    @SuppressWarnings("unchecked")
    public void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            personnes = (List<Personne>) ois.readObject();
            System.out.println("Users loaded successfully from " + USERS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found. Starting with an empty list.");
            personnes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            personnes = new ArrayList<>(); // Start fresh on error
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(personnes);
            System.out.println("Users saved successfully to " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    // Similar load/save methods for FARES_FILE (titresVendus) and COMPLAINTS_FILE (reclamations from ServiceReclamation)
    // For simplicity, only users are shown here. The actual implementation will need to handle all required data.

    public void loadAllData() {
        loadUsers();
        // loadFares();
        // loadComplaints();
    }

    public void saveAllData() {
        saveUsers();
        // saveFares();
        // saveComplaints();
    }
}

