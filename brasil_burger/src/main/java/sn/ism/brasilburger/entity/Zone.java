package sn.ism.brasilburger.entity;

import java.time.LocalDateTime;

public class Zone {
    private int id;
    private String nom;
    private String quartiers;
    private double prixLivraison;
    private LocalDateTime dateCreation;

    // Constructeurs
    public Zone() {}

    public Zone(String nom, String quartiers, double prixLivraison) {
        this.nom = nom;
        this.quartiers = quartiers;
        this.prixLivraison = prixLivraison;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getQuartiers() { return quartiers; }
    public void setQuartiers(String quartiers) { this.quartiers = quartiers; }

    public double getPrixLivraison() { return prixLivraison; }
    public void setPrixLivraison(double prixLivraison) { this.prixLivraison = prixLivraison; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() {
        return String.format("Zone{id=%d, nom='%s', quartiers='%s', prixLivraison=%.2f FCFA}",
                id, nom, quartiers, prixLivraison);
    }

}
