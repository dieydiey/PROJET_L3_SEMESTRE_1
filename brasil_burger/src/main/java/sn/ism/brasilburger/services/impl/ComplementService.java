package sn.ism.brasilburger.services.impl;

import sn.ism.brasilburger.entity.Complement;
import sn.ism.brasilburger.entity.enums.TypeComplement;
import sn.ism.brasilburger.services.interfaces.IComplementService;
import sn.ism.brasilburger.repositories.interfaces.IComplementRepository;
import sn.ism.brasilburger.exceptions.ValidationException;
import sn.ism.brasilburger.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class ComplementService implements IComplementService {
    private final IComplementRepository complementRepository;

    public ComplementService(IComplementRepository complementRepository) {
        this.complementRepository = complementRepository;
    }

    @Override
   public boolean creerComplement(String nom, TypeComplement type, double prix, String image) {
        validerNom(nom);
        validerType(type);
        validerPrix(prix);

        Complement complement = new Complement(nom, type, prix, image);
        return complementRepository.save(complement);
    }


    private void validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom du complément ne peut pas être vide");
        }
        if (nom.length() < 2) {
            throw new ValidationException("Le nom du complément doit contenir au moins 2 caractères");
        }
        if (nom.length() > 100) {
            throw new ValidationException("Le nom du complément ne peut pas dépasser 100 caractères");
        }
    }

    private void validerType(TypeComplement type) {
    if (type == null) {
        throw new ValidationException("Le type du complément ne peut pas être vide");
    }
}



    private void validerPrix(double prix) {
        if (prix <= 0) {
            throw new ValidationException("Le prix doit être supérieur à 0");
        }
        if (prix > 100000) {
            throw new ValidationException("Le prix semble anormalement élevé");
        }
    }

    @Override
    public boolean existeNom(String nom) {
        return complementRepository.findByNom(nom).isPresent();
    }

    @Override
    public List<Complement> listerTousComplements() {
        return complementRepository.findAll();
    }

    @Override
    public Complement obtenirComplement(int id) {
        Optional<Complement> complement = complementRepository.findById(id);
        return complement.orElseThrow(() -> new EntityNotFoundException("Complément non trouvé avec l'id: " + id));
    }
    @Override
    public boolean modifierComplement(int id, String nom, TypeComplement type, double prix, String image) {
        validerNom(nom);
        validerType(type);
        validerPrix(prix);
        Complement complement = obtenirComplement(id);
        complement.setNom(nom);
        complement.setType(type);
        complement.setPrix(prix);
        complement.setImage(image);
        return complementRepository.update(complement);
    }

    @Override
    public boolean archiverComplement(int id) {
        Complement complement = obtenirComplement(id);
        complement.setArchive(true);
        return complementRepository.update(complement);
    }

    @Override
    public boolean restaurerComplement(int id) {
        Complement complement = obtenirComplement(id);
        complement.setArchive(false);
        return complementRepository.update(complement);
    }

}
