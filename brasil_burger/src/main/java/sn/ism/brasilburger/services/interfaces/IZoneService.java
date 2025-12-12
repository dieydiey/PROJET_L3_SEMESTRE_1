package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Zone;
import java.util.List;

public interface IZoneService {
    boolean creerZone(String nom, String quartiers, double prixLivraison);
    public boolean existeNom(String nom);

}
