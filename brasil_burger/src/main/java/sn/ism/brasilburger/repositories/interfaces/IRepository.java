package sn.ism.brasilburger.repositories.interfaces;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    boolean save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean update(T entity);
    boolean delete(ID id);
}