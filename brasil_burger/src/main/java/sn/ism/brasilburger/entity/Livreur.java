package sn.ism.brasilburger.entity;

import java.time.LocalDateTime;

public class Livreur {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String matricule;
    private boolean disponible;
    private LocalDateTime dateCreation;

    // Constructeurs
    public Livreur() {
        this.disponible = true;
    }

    public Livreur(String nom, String prenom, String telephone, String matricule) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.matricule = matricule;
        this.disponible = true;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return String.format("Livreur{id=%d, nom='%s %s', matricule='%s', disponible=%s}",
                id, prenom, nom, matricule, disponible ? "Oui" : "Non");
    }
}
