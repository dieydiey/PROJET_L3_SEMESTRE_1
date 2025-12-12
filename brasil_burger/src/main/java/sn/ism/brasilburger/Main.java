package sn.ism.brasilburger;

import sn.ism.brasilburger.views.MenuPrincipal;
import sn.ism.brasilburger.services.impl.*;
import sn.ism.brasilburger.repositories.impl.*;
import sn.ism.brasilburger.services.interfaces.*;
import sn.ism.brasilburger.repositories.interfaces.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║         🍔 BRASIL BURGER - Système de Gestion       ║");
        System.out.println("║              Gestion des Ressources                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        
        IBurgerRepository burgerRepository = new BurgerRepositoryImpl();
        IMenuRepository menuRepository = new MenuRepositoryImpl();
        IComplementRepository complementRepository = new ComplementRepositoryImpl();
        IZoneRepository zoneRepository = new ZoneRepositoryImpl();
        ILivreurRepository livreurRepository = new LivreurRepositoryImpl();

        
        IBurgerService burgerService = new BurgerServiceImpl(burgerRepository);
        IMenuService menuService = new MenuServiceImpl(menuRepository, burgerRepository, complementRepository);
        IComplementService complementService = new ComplementService(complementRepository);
        IZoneService zoneService = new ZoneServiceImpl(zoneRepository);
        ILivreurService livreurService = new LivreurServiceImpl(livreurRepository);

        
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            burgerService,
            menuService,
            complementService,
            zoneService,
            livreurService
        );

        menuPrincipal.afficher();
    }
}