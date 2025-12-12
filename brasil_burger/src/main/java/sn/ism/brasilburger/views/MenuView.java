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
                        listerMenus();
                        break;
                    case 2:
                        //listerMenu();
                        break;
                    case 3:
                        //modifierMenu();
                        break;
                    case 4:
                        //archiverMenu();
                        break;
                    case 5:
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
        System.out.println("║  4. Archiver/Restaurer un menu                        ║");
        System.out.println("║  5. Voir détails d'un menu                            ║");
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


}
