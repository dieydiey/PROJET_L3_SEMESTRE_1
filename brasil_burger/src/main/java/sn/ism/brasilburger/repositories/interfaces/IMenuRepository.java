package sn.ism.brasilburger.repositories.interfaces;

import sn.ism.brasilburger.entity.Menu;
import sn.ism.brasilburger.entity.CompositionMenu;
import java.util.List;

public interface IMenuRepository extends IRepository<Menu, Integer> {
    boolean addComposition(CompositionMenu composition);
    List<CompositionMenu> findCompositionsByMenuId(Integer menuId);
    double calculatePrixTotal(Integer menuId);
    boolean toggleArchive(Integer id);
}
