package sn.ism.brasilburger.views;

import java.util.List;

import sn.ism.brasilburger.entity.*;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;
import sn.ism.brasilburger.services.interfaces.IBurgerService;
import sn.ism.brasilburger.services.interfaces.IComplementService;
import sn.ism.brasilburger.services.interfaces.IMenuService;


public class MenuView {
    private final IMenuService menuService;
    private final IBurgerService burgerService;
    private final IComplementService complementService;

    public MenuView(IMenuService menuService, IBurgerService burgerService, IComplementService complementService) {
        this.menuService = menuService;
        this.burgerService = burgerService;
        this.complementService = complementService;
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
                        ajouterMenu();
                        break;
                    case 2:
                        listerMenus();
                        break;
                    case 3:
                        modifierMenu();
                        break;
                    case 4:
                        archiverMenu();
                        break;
                    case 5:
                        restaurerMenu();
                        break;
                    case 6:
                        //voirDetailsMenu();
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
        System.out.println("║          📦 GESTION DES MENUS                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Lister tous les menus                            ║");
        System.out.println("║  2. Créer un menu                                     ║");
        System.out.println("║  3. Modifier un menu                                  ║");
        System.out.println("║  4. Archiver un menu                        ║");
        System.out.println("║  5. Restaurer un menu                            ║");
        System.out.println("║  6. Voir détails d'un menu                            ║");
        System.out.println("║  0. Retour au menu principal                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private void listerMenus() {
        ConsoleHelper.afficherTitre("LISTE DES MENUS");

        List<Menu> menus = menuService.listerTousMenus();

        if (menus.isEmpty()) {
            ConsoleHelper.afficherInfo("Aucun menu trouvé");
        } else {
            System.out.println("\n┌─────┬──────────────────────────┬─────────────┬──────────┐");
            System.out.println("│ ID  │ Nom                      │ Prix (FCFA) │ Statut   │");
            System.out.println("├─────┼──────────────────────────┼─────────────┼──────────┤");

            for (Menu menu : menus) {
                String statut = menu.isArchive() ? "Archivé" : "Actif";
                System.out.printf("│ %-3d │ %-24s │ %,11.0f │ %-8s │%n",
                    menu.getId(),
                    tronquer(menu.getNom(), 24),
                    menu.getPrixTotal(),
                    statut
                );
            }

            System.out.println("└─────┴──────────────────────────┴─────────────┴──────────┘");
            System.out.printf("\nTotal: %d menu(s)\n", menus.size());
        }

        ConsoleHelper.pause();
    }

    private String tronquer(String texte, int longueur) {
        if (texte.length() <= longueur) return texte;
        return texte.substring(0, longueur - 3) + "...";
    }

    private void ajouterMenu() {
        ConsoleHelper.afficherTitre("CRÉER UN MENU");

        try {
            String nom ;
            do {
                nom = ConsoleHelper.lireTexte("Nom du menu");

                if (menuService.existeNom(nom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (burgerService.existeNom(nom));
            String image = ConsoleHelper.lireTexte("Nom de l'image (ex: menu.jpg)");

            if (menuService.creerMenu(nom, image)) {
                ConsoleHelper.afficherSucces("Menu créé avec succès !");
                
                if (ConsoleHelper.confirmer("Voulez-vous ajouter des éléments au menu maintenant ?")) {
                    List<Menu> menus = menuService.listerTousMenus();
                    if (!menus.isEmpty()) {
                        int idMenu = menus.get(0).getId();
                        ajouterCompositions(idMenu);
                    }
                }
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

    private void ajouterCompositions(int idMenu) {
        boolean continuer = true;
        
        while (continuer) {
            ConsoleHelper.afficherSousTitre("Ajouter un élément au menu");
            System.out.println("1. Ajouter un burger");
            System.out.println("2. Ajouter un complément");
            System.out.println("0. Terminer");
            
            int choix = ConsoleHelper.lireEntier("Votre choix");
            
            switch (choix) {
                case 1:
                    ajouterBurgerAuMenu(idMenu);
                    break;
                case 2:
                    ajouterComplementAuMenu(idMenu);
                    break;
                case 0:
                    continuer = false;
                    break;
            }
        }
    }

    private void ajouterBurgerAuMenu(int idMenu) {
        List<Burger> burgers = burgerService.listerBurgersActifs();
        
        if (burgers.isEmpty()) {
            ConsoleHelper.afficherErreur("Aucun burger actif disponible");
            return;
        }
        
        System.out.println("\nBurgers disponibles:");
        for (Burger burger : burgers) {
            System.out.printf("  [%d] %s - %.0f FCFA%n", burger.getId(), burger.getNom(), burger.getPrix());
        }
        
        int idBurger = ConsoleHelper.lireEntier("ID du burger");
        int quantite = ConsoleHelper.lireEntier("Quantité");
        
        if (menuService.ajouterComposition(idMenu, idBurger, null, quantite)) {
            ConsoleHelper.afficherSucces("Burger ajouté au menu !");
        }
    }

    private void ajouterComplementAuMenu(int idMenu) {
        List<Complement> complements = complementService.listerComplementsActifs();
        
        if (complements.isEmpty()) {
            ConsoleHelper.afficherErreur("Aucun complément actif disponible");
            return;
        }
        
        System.out.println("\nCompléments disponibles:");
        for (Complement complement : complements) {
            System.out.printf("  [%d] %s (%s) - %.0f FCFA%n", 
                complement.getId(), complement.getNom(), complement.getType(), complement.getPrix());
        }
        
        int idComplement = ConsoleHelper.lireEntier("ID du complément");
        int quantite = ConsoleHelper.lireEntier("Quantité");
        
        if (menuService.ajouterComposition(idMenu, null, idComplement, quantite)) {
            ConsoleHelper.afficherSucces("Complément ajouté au menu !");
        }
    }

    private void modifierMenu() {
        ConsoleHelper.afficherTitre("MODIFIER UN MENU");

        Menu menu = null;

        while (menu == null) {
            try {
                int id = ConsoleHelper.lireEntier("ID du menu à modifier");
                menu = menuService.obtenirMenu(id); 

            } catch (EntityNotFoundException e) {
                ConsoleHelper.afficherErreur(e.getMessage());
                System.out.println("Veuillez saisir un ID valide.\n");
            }
        }

        System.out.println("\nMenu actuel: " + menu.getNom() + " - " + menu.getPrixTotal() + " FCFA");

        try {
            String nouveauNom ;
            do {
                nouveauNom = ConsoleHelper.lireTexte("Nouveau nom du menu");

                if (menuService.existeNom(nouveauNom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (menuService.existeNom(nouveauNom));

            String nouvelleImage = ConsoleHelper.lireTexte("Nouveau nom de l'image (ex: menu.jpg)");

            if (menuService.modifierMenu(menu.getId(), nouveauNom, nouvelleImage)) {
                ConsoleHelper.afficherSucces("Menu modifié avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

    private void archiverMenu() {
        ConsoleHelper.afficherTitre("ARCHIVER UN MENU");

        Menu menu = null;

        while (menu == null) {
            int id = ConsoleHelper.lireEntier("ID du menu à archiver");

            try {
                menu = menuService.obtenirMenu(id);
            } catch (EntityNotFoundException e) {
                ConsoleHelper.afficherErreur(e.getMessage());
            }
        }

        try {
            if (menuService.archiverMenu(menu.getId())) {
                ConsoleHelper.afficherSucces("Menu archivé avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

    private void restaurerMenu() {
        ConsoleHelper.afficherTitre("RESTAURER UN MENU");

        Menu menu = null;

        while (menu == null) {
            int id = ConsoleHelper.lireEntier("ID du menu à restaurer");

            try {
                menu = menuService.obtenirMenu(id);
            } catch (EntityNotFoundException e) {
                ConsoleHelper.afficherErreur(e.getMessage());
            }
        }

        try {
            if (menuService.restaurerMenu(menu.getId())) {
                ConsoleHelper.afficherSucces("Menu restauré avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

}
