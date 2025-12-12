package sn.ism.brasilburger.services.impl;

import sn.ism.brasilburger.entity.Livreur;
import sn.ism.brasilburger.services.interfaces.ILivreurService;
import sn.ism.brasilburger.repositories.interfaces.ILivreurRepository;
import sn.ism.brasilburger.exceptions.ValidationException;
import sn.ism.brasilburger.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class LivreurServiceImpl implements ILivreurService {
    private final ILivreurRepository livreurRepository;

    public LivreurServiceImpl(ILivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }

    @Override
    public boolean creerLivreur(String nom, String prenom, String telephone) {

        validerNom(nom);
        validerPrenom(prenom);
        validerTelephone(telephone);
        verifierUniciteTelephone(telephone);
        String matricule = genererMatricule();
        Livreur livreur = new Livreur();
        livreur.setNom(nom.trim());
        livreur.setPrenom(prenom.trim());
        livreur.setTelephone(telephone.trim());
        livreur.setDisponible(true);
        livreur.setMatricule(matricule);
        livreur.setDisponible(false);

        return livreurRepository.save(livreur);
    }

    

   

    private void validerNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new ValidationException("Le nom du livreur est obligatoire.");
        }
    }

    private void validerPrenom(String prenom) {
        if (prenom == null || prenom.isBlank()) {
            throw new ValidationException("Le prénom du livreur est obligatoire.");
        }
    }

    private void validerTelephone(String telephone) {
        if (telephone == null || telephone.isBlank()) {
            throw new ValidationException("Le téléphone du livreur est obligatoire.");
        }

        if (!telephone.matches("^(77|78|75|76|70)[0-9]{7}$")) {
            throw new ValidationException("Téléphone invalide (format Sénégal attendu).");
        }
    }

    /**
     * Valide le format du téléphone (sans lever d'exception)
     * @param telephone le numéro à valider
     * @return true si le format est valide, false sinon
     */
    public boolean validerTelephoneFormat(String telephone) {
        if (telephone == null || telephone.isBlank()) {
            return false;
        }
        return telephone.matches("^(77|78|75|76|70)[0-9]{7}$");
    }

   
    private void verifierUniciteTelephone(String telephone) {
        Optional<Livreur> exist = livreurRepository.findByTelephone(telephone);
        if (exist.isPresent()) {
            throw new ValidationException("Ce numéro de téléphone existe déjà.");
        }
    }



    private String genererMatricule() {
        List<Livreur> livreurs = livreurRepository.findAll();

        int max = 0;
        for (Livreur l : livreurs) {
            if (l.getMatricule() != null && l.getMatricule().startsWith("LIV")) {
                try {
                    int num = Integer.parseInt(l.getMatricule().substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }

        int nouveauNumero = max + 1;
        return String.format("LIV%03d", nouveauNumero);
    }

    @Override
    public List<Livreur> listerTousLivreurs() {
        return livreurRepository.findAll();
    }
    
}
