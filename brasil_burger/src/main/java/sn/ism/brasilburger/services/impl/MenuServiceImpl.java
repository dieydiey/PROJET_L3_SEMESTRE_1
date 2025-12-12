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
    
}
