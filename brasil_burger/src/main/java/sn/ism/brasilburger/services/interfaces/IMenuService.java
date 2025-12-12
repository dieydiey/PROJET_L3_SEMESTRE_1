package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Menu;
import sn.ism.brasilburger.entity.CompositionMenu;
import java.util.List;

public interface IMenuService {
    List<Menu> listerTousMenus();
}
