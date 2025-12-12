package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Livreur;
import java.util.List;

public interface ILivreurService {
    public boolean creerLivreur(String nom, String prenom, String telephone);
    public List<Livreur> listerTousLivreurs();
}
