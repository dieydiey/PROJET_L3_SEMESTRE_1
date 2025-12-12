package sn.ism.brasilburger.repositories.interfaces;

import sn.ism.brasilburger.entity.Livreur;

import java.util.List;
import java.util.Optional;

public interface ILivreurRepository {
    boolean save(Livreur livreur);
    Optional<Livreur> findByTelephone(String telephone); 
    public List<Livreur> findAll();
}