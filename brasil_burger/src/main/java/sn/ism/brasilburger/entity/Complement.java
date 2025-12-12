package sn.ism.brasilburger.entity;

import sn.ism.brasilburger.entity.enums.TypeComplement;
import java.time.LocalDateTime;

public class Complement {
    private int id;
    private String nom;
    private TypeComplement type;
    private double prix;
    private String image;
    private boolean archive;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;


    public Complement() {}

    public Complement(String nom, TypeComplement type, double prix, String image) {
        this.nom = nom;
        this.type = type;
        this.prix = prix;
        this.image = image;
        this.archive = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeComplement getType() { return type; }
    public void setType(TypeComplement type) { this.type = type; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public boolean isArchive() { return archive; }
    public void setArchive(boolean archive) { this.archive = archive; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    @Override
    public String toString() {
        return String.format("Complement{id=%d, nom='%s', type='%s', prix=%.2f FCFA, archive=%s}",
            id, nom, type, prix, archive ? "Oui" : "Non");
    }
}
