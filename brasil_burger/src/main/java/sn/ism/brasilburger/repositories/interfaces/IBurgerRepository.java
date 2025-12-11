package sn.ism.brasilburger.repositories.interfaces;

import  sn.ism.brasilburger.entity.Burger;
import java.util.List;
import java.util.Optional;

public interface IBurgerRepository extends IRepository<Burger, Integer> {
    boolean save(Burger burger);
    Optional<Burger> findByNom(String nom);
    public List<Burger> findAll();
    List<Burger> findActifs();
    Optional<Burger> findById(Integer id);
    public boolean toggleArchive(Integer id);
    public boolean delete(Integer id); 
    public boolean update(Burger burger);


}
