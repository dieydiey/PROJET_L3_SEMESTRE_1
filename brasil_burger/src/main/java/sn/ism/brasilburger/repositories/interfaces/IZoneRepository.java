package sn.ism.brasilburger.repositories.interfaces;

import  sn.ism.brasilburger.entity.Zone;
import java.util.List;
import java.util.Optional;

public interface IZoneRepository {
    public boolean save(Zone zone);
    public List<Zone> findAll();
    public Optional<Zone> findByNom(String nom);
}
