package sn.ism.brasilburger.views;

import sn.ism.brasilburger.services.interfaces.*;
import sn.ism.brasilburger.utils.ConsoleHelper;

public class MenuPrincipal {
    private final IBurgerService burgerService;
    private final IMenuService menuService;
    private final IComplementService complementService;
    private final IZoneService zoneService;
    private final ILivreurService livreurService;

    private final BurgerView burgerView;
    private final MenuView menuView;
    private final ComplementView complementView;
    private final ZoneView zoneView;
    private final LivreurView livreurView;

    public MenuPrincipal(IBurgerService burgerService, 
                        IMenuService menuService,
                        IComplementService complementService,
                        IZoneService zoneService,
                        ILivreurService livreurService) {
        this.burgerService = burgerService;
        this.menuService = menuService;
        this.complementService = complementService;
        this.zoneService = zoneService;
        this.livreurService = livreurService;

        // Initialisation des vues
        this.burgerView = new BurgerView(burgerService);
        this.menuView = new MenuView(menuService, burgerService, complementService);
        this.complementView = new ComplementView(complementService);
        this.zoneView = new ZoneView(zoneService);
        this.livreurView = new LivreurView(livreurService);
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleHelper.clearScreen();
                afficherMenu();
                int choix = ConsoleHelper.lireEntier("Votre choix");

                switch (choix) {
                    case 1:
                        burgerView.afficher();
                        break;
                    case 2:
                        menuView.afficher();
                        break;
                    case 3:
                        complementView.afficher();
                        break;
                    case 4:
                        zoneView.afficher();
                        break;
                    case 5:
                        livreurView.afficher();
                        break;
                    
                    case 0:
                        continuer = confirmerQuitter();
                        break;
                    default:
                        ConsoleHelper.afficherErreur("Choix invalide");
                        ConsoleHelper.pause();
                }
            } catch (Exception e) {
                ConsoleHelper.afficherErreur("Une erreur s'est produite: " + e.getMessage());
                ConsoleHelper.pause();
            }
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║       Merci d'avoir utilisé Brasil Burger ! 👋       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
    }

    private void afficherMenu() {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              MENU PRINCIPAL                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1. 🍔 Gestion des Burgers                           ║");
        System.out.println("║  2. 📦 Gestion des Menus                             ║");
        System.out.println("║  3. 🍟 Gestion des Compléments                       ║");
        System.out.println("║  4. 📍 Gestion des Zones                             ║");
        System.out.println("║  5. 🚗 Gestion des Livreurs                          ║");
        System.out.println("║  0. 🚪 Quitter                                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private boolean confirmerQuitter() {
        return !ConsoleHelper.confirmer("Êtes-vous sûr de vouloir quitter ?");
    }

}
