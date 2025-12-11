package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Burger;

public interface IBurgerService {
    boolean creerBurger(String nom, double prix, String image);
    public boolean existeNom(String nom);
}
