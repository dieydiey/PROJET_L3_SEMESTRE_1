package sn.ism.brasilburger.repositories.interfaces;

import sn.ism.brasilburger.entity.Menu;
import sn.ism.brasilburger.entity.CompositionMenu;
import java.util.List;
import java.util.Optional;

public interface IMenuRepository extends IRepository<Menu, Integer> {
    boolean addComposition(CompositionMenu composition);
    List<CompositionMenu> findCompositionsByMenuId(Integer menuId);
    double calculatePrixTotal(Integer menuId);
    boolean toggleArchive(Integer id);
    public boolean save(Menu menu);
    public List<Menu> findAll();
    public Optional<Menu> findById(Integer id);
    public Optional<Menu> findByNom(String nom);
    public boolean update(Menu menu);
    public boolean delete(Integer id);
    public List<Menu> findActifs();
    
}
