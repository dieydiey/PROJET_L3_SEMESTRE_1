package sn.ism.brasilburger.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import sn.ism.brasilburger.entity.Complement;

public interface IComplementRepository extends IRepository<Complement, Integer>{
    public boolean save(Complement complement);
    public List<Complement> findAll();
    public Optional<Complement> findById(Integer id); 
    public Optional<Complement> findByNom(String nom);
    public List<Complement> findByType(String type);
    public List<Complement> findActifs();
    public boolean toggleArchive(Integer id);   
    public boolean update(Complement complement);
}
