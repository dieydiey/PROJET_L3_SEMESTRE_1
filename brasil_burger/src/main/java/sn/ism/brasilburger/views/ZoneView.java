package sn.ism.brasilburger.views;

import sn.ism.brasilburger.entity.Zone;
import sn.ism.brasilburger.services.interfaces.IZoneService;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;

import java.util.List;

public class ZoneView {
    private final IZoneService zoneService;

    public ZoneView(IZoneService zoneService) {
        this.zoneService = zoneService;
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
                        ajouterZones();
                        break;
                    case 2:
                        //listerZone();
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
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          📍 GESTION DES ZONES                         ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Ajouter unevzones                                 ║");
        System.out.println("║  2. Lister toutes les zones                           ║");
        System.out.println("║  0. Retour au menu principal                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
    private void ajouterZones() {
        ConsoleHelper.afficherTitre("AJOUTER UNE ZONE");

        try {
            String nom;
            do {
                nom = ConsoleHelper.lireTexte("Nom de la zone");

                if (zoneService.existeNom(nom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (zoneService.existeNom(nom));
            String quartiers = ConsoleHelper.lireTexte("Quartiers (séparés par des virgules)");
            double prixLivraison = ConsoleHelper.lireDecimal("Prix de livraison (FCFA)");

            if (zoneService.creerZone(nom, quartiers, prixLivraison)) {
                ConsoleHelper.afficherSucces("Zone créée avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }
}
