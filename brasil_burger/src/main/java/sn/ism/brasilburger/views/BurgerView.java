package sn.ism.brasilburger.views;

import sn.ism.brasilburger.entity.Burger;
import sn.ism.brasilburger.services.interfaces.IBurgerService;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;

public class BurgerView {
    private final IBurgerService burgerService;

    public BurgerView(IBurgerService burgerService) {
        this.burgerService = burgerService;
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
                       // listerBurgers();
                        break;
                    case 2:
                        ajouterBurger();
                        break;
                    case 3:
                       // modifierBurger();
                        break;
                    case 4:
                        //archiverBurger();
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
        System.out.println("║          🍔 GESTION DES BURGERS                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Lister tous les burgers                          ║");
        System.out.println("║  2. Ajouter un burger                                 ║");
        System.out.println("║  3. Modifier un burger                                ║");
        System.out.println("║  4. Archiver/Restaurer un burger                      ║");
        System.out.println("║  5. Rechercher un burger                              ║");
        System.out.println("║  0. Retour au menu principal                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
    private void ajouterBurger() {
        ConsoleHelper.afficherTitre("AJOUTER UN BURGER");

        try {
            String nom;
            do {
                nom = ConsoleHelper.lireTexte("Nom du burger");

                if (burgerService.existeNom(nom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (burgerService.existeNom(nom));

            double prix = ConsoleHelper.lireDecimal("Prix (FCFA)");
            String image = ConsoleHelper.lireTexte("Nom de l'image (ex: burger.jpg)");

            if (burgerService.creerBurger(nom, prix, image)) {
                ConsoleHelper.afficherSucces("Burger créé avec succès !");
            } else {
                ConsoleHelper.afficherErreur("Échec de la création du burger");
            }

        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }


}
