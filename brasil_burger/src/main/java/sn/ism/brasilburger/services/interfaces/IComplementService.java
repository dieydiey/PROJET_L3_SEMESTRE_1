package sn.ism.brasilburger.services.interfaces;

import sn.ism.brasilburger.entity.Complement;
import sn.ism.brasilburger.entity.enums.TypeComplement;

import java.util.List;

public interface IComplementService {
    boolean creerComplement(String nom, TypeComplement type, double prix, String image);
    public boolean existeNom(String nom);
    List<Complement> listerTousComplements();
    Complement obtenirComplement(int id);
    boolean modifierComplement(int id, String nom, TypeComplement type, double prix, String image);
    boolean archiverComplement(int id);

}
