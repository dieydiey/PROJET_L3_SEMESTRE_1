package sn.ism.brasilburger.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private int id;
    private String nom;
    private String image;
    private boolean archive;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private List<CompositionMenu> compositions;
    private double prixTotal;

    // Constructeurs
    public Menu() {
        this.compositions = new ArrayList<>();
    }

    public Menu(String nom, String image) {
        this.nom = nom;
        this.image = image;
        this.archive = false;
        this.compositions = new ArrayList<>();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public boolean isArchive() { return archive; }
    public void setArchive(boolean archive) { this.archive = archive; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public List<CompositionMenu> getCompositions() { return compositions; }
    public void setCompositions(List<CompositionMenu> compositions) { this.compositions = compositions; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    public void ajouterComposition(CompositionMenu composition) {
        this.compositions.add(composition);
    }

    @Override
    public String toString() {
        return String.format("Menu{id=%d, nom='%s', prixTotal=%.2f FCFA, archive=%s}",
                id, nom, prixTotal, archive ? "Oui" : "Non");
    }

}
