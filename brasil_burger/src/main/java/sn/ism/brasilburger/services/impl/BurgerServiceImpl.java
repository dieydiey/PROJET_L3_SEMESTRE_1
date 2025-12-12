package sn.ism.brasilburger.services.impl;

import sn.ism.brasilburger.entity.Burger;
import sn.ism.brasilburger.services.interfaces.IBurgerService;
import sn.ism.brasilburger.repositories.interfaces.IBurgerRepository;
import sn.ism.brasilburger.exceptions.ValidationException;
import sn.ism.brasilburger.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class BurgerServiceImpl implements IBurgerService {
    private final IBurgerRepository burgerRepository;

     public BurgerServiceImpl(IBurgerRepository burgerRepository) {
        this.burgerRepository = burgerRepository;
    }

    @Override
    public boolean creerBurger(String nom, double prix, String image) {
        // Validation des données (Business Logic)
        validerNom(nom);
        validerPrix(prix);
        
        Burger burger = new Burger(nom, prix, image);
        return burgerRepository.save(burger);
    }


    private void validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom du burger ne peut pas être vide");
        }
        if (nom.length() < 3) {
            throw new ValidationException("Le nom du burger doit contenir au moins 3 caractères");
        }
        if (nom.length() > 100) {
            throw new ValidationException("Le nom du burger ne peut pas dépasser 100 caractères");
        }
    }

    private void validerPrix(double prix) {
        if (prix <= 0) {
            throw new ValidationException("Le prix doit être supérieur à 0");
        }
        if (prix > 1000000) {
            throw new ValidationException("Le prix semble anormalement élevé");
        }
    }

    @Override
    public boolean existeNom(String nom) {
        return burgerRepository.findByNom(nom).isPresent();
    }

     @Override
    public List<Burger> listerTousBurgers() {
        return burgerRepository.findAll();
    }

    @Override
    public Burger obtenirBurger(int id) {
        Optional<Burger> burger = burgerRepository.findById(id);
        return burger.orElseThrow(() -> new EntityNotFoundException("Burger non trouvé avec l'id: " + id));
    }

     @Override
    public boolean modifierBurger(int id, String nom, double prix, String image) {
        validerNom(nom);
        validerPrix(prix);
        Burger burger = obtenirBurger(id);
        burger.setNom(nom);
        burger.setPrix(prix);
        burger.setImage(image);
        return burgerRepository.update(burger);
    }

     @Override
    public boolean archiverBurger(int id) {
        Burger burger = obtenirBurger(id);
        burger.setArchive(true);
        return burgerRepository.update(burger);
    }

    @Override
    public boolean restaurerBurger(int id) {
        Burger burger = obtenirBurger(id);
        burger.setArchive(false);
        return burgerRepository.update(burger);
    }

    

}
