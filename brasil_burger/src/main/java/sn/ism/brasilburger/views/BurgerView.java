package sn.ism.brasilburger.views;

import sn.ism.brasilburger.entity.Burger;
import sn.ism.brasilburger.services.interfaces.IBurgerService;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;
import java.util.List;

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
                        ajouterBurger();
                       
                        break;
                    case 2:
                        listerBurgers();
                        break;
                    case 3:
                       modifierBurger();
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
        System.out.println("║  1. Ajouter un burger                                 ║");
        System.out.println("║  2. Lister tous les burgers                           ║");
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

    private void listerBurgers() {
        ConsoleHelper.afficherTitre("LISTE DES BURGERS");

        List<Burger> burgers = burgerService.listerTousBurgers();

        if (burgers.isEmpty()) {
            ConsoleHelper.afficherInfo("Aucun burger trouvé");
        } else {
            System.out.println("\n┌─────┬──────────────────────────┬─────────────┬──────────┐");
            System.out.println("│ ID  │ Nom                      │ Prix (FCFA) │ Statut   │");
            System.out.println("├─────┼──────────────────────────┼─────────────┼──────────┤");

            for (Burger burger : burgers) {
                String statut = burger.isArchive() ? "Archivé" : "Actif";
                System.out.printf("│ %-3d │ %-24s │ %,11.0f │ %-8s │%n",
                    burger.getId(),
                    tronquer(burger.getNom(), 24),
                    burger.getPrix(),
                    statut
                );
            }

            System.out.println("└─────┴──────────────────────────┴─────────────┴──────────┘");
            System.out.printf("\nTotal: %d burger(s)\n", burgers.size());
        }
    }

     private String tronquer(String texte, int longueur) {
        if (texte.length() <= longueur) {
            return texte;
        }
        return texte.substring(0, longueur - 3) + "...";
    }

    private void modifierBurger() {
    ConsoleHelper.afficherTitre("MODIFIER UN BURGER");

    Burger burger = null;

    // 🔁 Boucle jusqu'à obtenir un ID existant
    while (burger == null) {
        try {
            int id = ConsoleHelper.lireEntier("ID du burger à modifier");
            burger = burgerService.obtenirBurger(id); // lance exception si non trouvé

        } catch (EntityNotFoundException e) {
            ConsoleHelper.afficherErreur(e.getMessage());
            System.out.println("Veuillez saisir un ID valide.\n");
        }
    }

    // 🎉 À partir d'ici, "burger" est forcément valide
    System.out.println("\nBurger actuel: " + burger.getNom() + " - " + burger.getPrix() + " FCFA");

    try {
        String nom = ConsoleHelper.lireTexte("Nouveau nom [" + burger.getNom() + "]");
        if (nom.isEmpty()) nom = burger.getNom();

        System.out.print("Nouveau prix [" + burger.getPrix() + "]: ");
        String prixStr = ConsoleHelper.lireTexte("");
        double prix = prixStr.isEmpty() ? burger.getPrix() : Double.parseDouble(prixStr);

        String image = ConsoleHelper.lireTexte("Nouvelle image [" + burger.getImage() + "]");
        if (image.isEmpty()) image = burger.getImage();

        if (burgerService.modifierBurger(burger.getId(), nom, prix, image)) {
            ConsoleHelper.afficherSucces("Burger modifié avec succès !");
        }

    } catch (ValidationException e) {
        ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
    }

    ConsoleHelper.pause();
}



}
