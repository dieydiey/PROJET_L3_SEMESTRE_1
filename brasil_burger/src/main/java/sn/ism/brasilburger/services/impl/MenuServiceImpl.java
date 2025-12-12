package sn.ism.brasilburger.services.impl;

import sn.ism.brasilburger.entity.Menu;
import sn.ism.brasilburger.entity.CompositionMenu;
import sn.ism.brasilburger.services.interfaces.IMenuService;
import sn.ism.brasilburger.repositories.interfaces.IMenuRepository;
import sn.ism.brasilburger.repositories.interfaces.IBurgerRepository;
import sn.ism.brasilburger.repositories.interfaces.IComplementRepository;
import sn.ism.brasilburger.exceptions.ValidationException;
import sn.ism.brasilburger.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements IMenuService{
     private final IMenuRepository menuRepository;
    private final IBurgerRepository burgerRepository;
    private final IComplementRepository complementRepository;

    public MenuServiceImpl(IMenuRepository menuRepository, 
                          IBurgerRepository burgerRepository,
                          IComplementRepository complementRepository) {
        this.menuRepository = menuRepository;
        this.burgerRepository = burgerRepository;
        this.complementRepository = complementRepository;
    }

    @Override
    public List<Menu> listerTousMenus() {
        return menuRepository.findAll();
    }

    @Override
    public boolean existeNom(String nom) {
        return burgerRepository.findByNom(nom).isPresent();
    }

    @Override
    public boolean creerMenu(String nom, String image) {
        validerNom(nom);
        
        Menu menu = new Menu(nom, image);
        return menuRepository.save(menu);
    }

    private void validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom du menu ne peut pas être vide");
        }
        if (nom.length() < 3) {
            throw new ValidationException("Le nom du menu doit contenir au moins 3 caractères");
        }
        if (nom.length() > 100) {
            throw new ValidationException("Le nom du menu ne peut pas dépasser 100 caractères");
        }
    }

    @Override
    public double calculerPrixMenu(int idMenu) {
        obtenirMenu(idMenu); // Vérifie l'existence
        return menuRepository.calculatePrixTotal(idMenu);
    }

    @Override
    public Menu obtenirMenu(int id) {
        Optional<Menu> menu = menuRepository.findById(id);
        return menu.orElseThrow(() -> 
            new EntityNotFoundException("Menu avec l'ID " + id + " introuvable"));
    }

    @Override
    public boolean ajouterComposition(int idMenu, Integer idBurger, Integer idComplement, int quantite) {
        obtenirMenu(idMenu);
        
        if (idBurger == null && idComplement == null) {
            throw new ValidationException("Vous devez spécifier soit un burger, soit un complément");
        }
        
        if (idBurger != null && idComplement != null) {
            throw new ValidationException("Vous ne pouvez pas ajouter un burger et un complément en même temps");
        }
        
        if (quantite <= 0) {
            throw new ValidationException("La quantité doit être supérieure à 0");
        }
        
        if (idBurger != null && !burgerRepository.findById(idBurger).isPresent()) {
            throw new EntityNotFoundException("Burger avec l'ID " + idBurger + " introuvable");
        }
        
        if (idComplement != null && !complementRepository.findById(idComplement).isPresent()) {
            throw new EntityNotFoundException("Complément avec l'ID " + idComplement + " introuvable");
        }
        
        CompositionMenu composition = new CompositionMenu(idMenu, idBurger, idComplement, quantite);
        return menuRepository.addComposition(composition);
    }

    
}
