package sn.ism.brasilburger.services.impl;

import sn.ism.brasilburger.entity.Zone;
import sn.ism.brasilburger.services.interfaces.IZoneService;
import sn.ism.brasilburger.repositories.interfaces.IZoneRepository;
import sn.ism.brasilburger.exceptions.ValidationException;
import sn.ism.brasilburger.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class ZoneServiceImpl implements IZoneService{
    private final IZoneRepository zoneRepository;

    public ZoneServiceImpl(IZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public boolean creerZone(String nom, String quartiers, double prixLivraison) {
        validerNom(nom);
        validerQuartiers(quartiers);
        validerPrixLivraison(prixLivraison);
        
        Zone zone = new Zone(nom, quartiers, prixLivraison);
        return zoneRepository.save(zone);
    }
    private void validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom de la zone ne peut pas être vide");
        }
        if (nom.length() < 3) {
            throw new ValidationException("Le nom de la zone doit contenir au moins 3 caractères");
        }
        if (nom.length() > 100) {
            throw new ValidationException("Le nom de la zone ne peut pas dépasser 100 caractères");
        }
    }
    private void validerQuartiers(String quartiers) {
        if (quartiers == null || quartiers.trim().isEmpty()) {
            throw new ValidationException("Les quartiers ne peuvent pas être vides");
        }
        if (quartiers.length() < 3) {
            throw new ValidationException("Les quartiers doivent contenir au moins 3 caractères");
        }
        if (quartiers.length() > 255) {
            throw new ValidationException("Les quartiers ne peuvent pas dépasser 255 caractères");
        }
    }
    private void validerPrixLivraison(double prixLivraison) {
        if (prixLivraison < 0) {
            throw new ValidationException("Le prix de livraison ne peut pas être négatif");
        }
        if (prixLivraison > 100000) {
            throw new ValidationException("Le prix de livraison semble anormalement élevé");
        }
    }

     @Override
    public boolean existeNom(String nom) {
        return zoneRepository.findByNom(nom).isPresent();
    }


}
