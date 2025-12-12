package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Menu;
import sn.ism.brasilburger.entity.CompositionMenu;
import java.util.List;

public interface IMenuService {
    List<Menu> listerTousMenus();
    public boolean existeNom(String nom);
    boolean creerMenu(String nom, String image);
    boolean ajouterComposition(int idMenu, Integer idBurger, Integer idComplement, int quantite);
    public Menu obtenirMenu(int id);
    public double calculerPrixMenu(int idMenu);
    boolean modifierMenu(int id, String nom, String image);
}
