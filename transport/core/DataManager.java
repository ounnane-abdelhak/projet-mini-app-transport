package transport.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Personne> personnes;
    private List<TitreTransport> titresVendus;

    private static final String USERS_FILE = "users.dat";
    private static final String FARES_FILE = "fares.dat";

    public DataManager() {
        this.personnes = new ArrayList<>();
        this.titresVendus = new ArrayList<>();
    }

    public void addPersonne(Personne personne) {
        if (personne != null) {
            this.personnes.add(personne);

        }
    }

    public List<Personne> getPersonnes() {
        return new ArrayList<>(this.personnes);
    }

    @SuppressWarnings("unchecked")
    public void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            this.personnes = (List<Personne>) ois.readObject();
        } catch (FileNotFoundException e) {
            this.personnes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.personnes = new ArrayList<>();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(this.personnes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTitreVendu(TitreTransport titre) {
        if (titre != null) {
            this.titresVendus.add(titre);
        }
    }

    public List<TitreTransport> getTitresVendus() {
        return new ArrayList<>(this.titresVendus);
    }

    @SuppressWarnings("unchecked")
    public void loadFares() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FARES_FILE))) {
            this.titresVendus = (List<TitreTransport>) ois.readObject();
        } catch (FileNotFoundException e) {
            this.titresVendus = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.titresVendus = new ArrayList<>();
        }
    }

    public void saveFares() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FARES_FILE))) {
            oos.writeObject(this.titresVendus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllData() {
        loadUsers();
        loadFares();
    }

    public void saveAllData() {
        saveUsers();
        saveFares();
    }
}

