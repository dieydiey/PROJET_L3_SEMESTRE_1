package sn.ism.brasilburger.views;

import sn.ism.brasilburger.entity.Livreur;
import sn.ism.brasilburger.services.interfaces.ILivreurService;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;

import java.util.List;

public class LivreurView {
    private final ILivreurService livreurService;

    public LivreurView(ILivreurService livreurService) {
        this.livreurService = livreurService;
    }

    public void afficher() {
        boolean retour = false;

        while (!retour) {
            try {
                ConsoleHelper.clearScreen();
                afficherMenu();
                int choix = ConsoleHelper.lireEntier("Votre choix");

                switch (choix) {
                    case 1:
                        ajouterLivreur();
                        break;
                    case 2:
                        listerLivreurs();
                        break;
                    case 0:
                        retour = true;
                        break;
                    default:
                        ConsoleHelper.afficherErreur("Choix invalide");
                        ConsoleHelper.pause();
                }
            } catch (Exception e) {
                ConsoleHelper.afficherErreur(e.getMessage());
                ConsoleHelper.pause();
            }
        }
    }

    private void afficherMenu() {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          🚗 GESTION DES LIVREURS                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Ajouter un livreurs                               ║");
        System.out.println("║  2. Lister les livreurs                               ║");
        System.out.println("║  0. Retour au menu principal                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private void ajouterLivreur() {
        ConsoleHelper.afficherTitre("AJOUT D'UN LIVREUR");

        String nom = ConsoleHelper.lireTexte("Nom du livreur");
        String prenom = ConsoleHelper.lireTexte("Prénom du livreur");
        String telephone = ConsoleHelper.lireTexte("Téléphone du livreur");

        boolean success = livreurService.creerLivreur(nom, prenom, telephone);
        if (success) {
            ConsoleHelper.afficherSucces("Livreur ajouté avec succès !");
        } else {
            ConsoleHelper.afficherErreur("Échec de l'ajout du livreur.");
        }
        ConsoleHelper.pause();
    }

    private void listerLivreurs() {
        ConsoleHelper.afficherTitre("LISTE DES LIVREURS");

        List<Livreur> livreurs = livreurService.listerTousLivreurs();
        if (livreurs.isEmpty()) {
            ConsoleHelper.afficherInfo("Aucun livreur disponible.");
        } else {
            System.out.printf("%-5s %-15s %-15s %-15s %-10s%n", "ID", "Nom", "Prénom", "Téléphone", "Disponible");
            System.out.println("--------------------------------------------------------------");
            for (Livreur livreur : livreurs) {
                System.out.printf("%-5d %-15s %-15s %-15s %-10s%n",
                        livreur.getId(),
                        livreur.getNom(),
                        livreur.getPrenom(),
                        livreur.getTelephone(),
                        livreur.isDisponible() ? "Oui" : "Non");
            }
        }
        ConsoleHelper.pause();
    }

}
