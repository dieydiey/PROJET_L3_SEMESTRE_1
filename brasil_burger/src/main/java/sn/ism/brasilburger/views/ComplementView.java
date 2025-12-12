package sn.ism.brasilburger.views;

import sn.ism.brasilburger.entity.Complement;
import sn.ism.brasilburger.entity.enums.TypeComplement;
import sn.ism.brasilburger.services.interfaces.IComplementService;
import sn.ism.brasilburger.utils.ConsoleHelper;
import sn.ism.brasilburger.exceptions.*;

import java.util.List;

public class ComplementView {
    private final IComplementService complementService;

    public ComplementView(IComplementService complementService) {
        this.complementService = complementService;
    }

    public void afficher() {
        boolean retour = false;

        while (!retour) {
            try {
                ConsoleHelper.clearScreen();
                afficherMenu();
                int choix = ConsoleHelper.lireEntier("Votre choix");

                switch (choix) {
                    case 1:
                        ajouterComplement();
                        
                        break;
                    case 2:
                        listerComplements();
                        break;
                    case 3:
                        modifierComplement();
                        break;
                    case 4:
                        archiverComplement();
                        break;
                    case 5:
                        //filtrerParType();
                        break;
                    case 0:
                        retour = true;
                        break;
                    default:
                        ConsoleHelper.afficherErreur("Choix invalide");
                        ConsoleHelper.pause();
                }
            } catch (Exception e) {
                ConsoleHelper.afficherErreur(e.getMessage());
                ConsoleHelper.pause();
            }
        }
    }

    private void afficherMenu() {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          🍟 GESTION DES COMPLÉMENTS                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  1.Ajouter un complément                              ║");
        System.out.println("║  2.Lister tous les compléments                        ║");
        System.out.println("║  3. Modifier un complément                            ║");
        System.out.println("║  4. Archiver un complément                            ║");
        System.out.println("║  5. Restaurer un complément                           ║");
        System.out.println("║  0. Retour au menu principal                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private void ajouterComplement() {
        ConsoleHelper.afficherTitre("AJOUTER UN COMPLÉMENT");

        try {
            String nom;
            do {
                nom = ConsoleHelper.lireTexte("Nom du complément");

                if (complementService.existeNom(nom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (complementService.existeNom(nom));

            System.out.println("\nType de complément:");
            System.out.println("  1. Boisson");
            System.out.println("  2. Frites");

            int typeChoix;
            do {
                typeChoix = ConsoleHelper.lireEntier("Votre choix");
            } while (typeChoix != 1 && typeChoix != 2);

            TypeComplement type = (typeChoix == 1)
                ? TypeComplement.BOISSON
                : TypeComplement.FRITES;

            double prix = ConsoleHelper.lireDecimal("Prix (FCFA)");
            String image = ConsoleHelper.lireTexte("Nom de l'image (ex: coca.jpg)");

            if (complementService.creerComplement(nom, type, prix, image)) {
                ConsoleHelper.afficherSucces("Complément créé avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

    private void listerComplements() {
        ConsoleHelper.afficherTitre("LISTE DES COMPLÉMENTS");

        List<Complement> complements = complementService.listerTousComplements();

        if (complements.isEmpty()) {
            ConsoleHelper.afficherInfo("Aucun complément trouvé");
        } else {
            System.out.println("\n┌─────┬──────────────────────────┬──────────┬─────────────┬──────────┐");
            System.out.println("│ ID  │ Nom                      │ Type     │ Prix (FCFA) │ Statut   │");
            System.out.println("├─────┼──────────────────────────┼──────────┼─────────────┼──────────┤");

            for (Complement complement : complements) {
                String statut = complement.isArchive() ? "Archivé" : "Actif";
                System.out.printf("│ %-3d │ %-24s │ %-8s │ %,11.0f │ %-8s │%n",
                    complement.getId(),
                    tronquer(complement.getNom(), 24),
                    complement.getType(),
                    complement.getPrix(),
                    statut
                );
            }

            System.out.println("└─────┴──────────────────────────┴──────────┴─────────────┴──────────┘");
            System.out.printf("\nTotal: %d complément(s)\n", complements.size());
        }

        ConsoleHelper.pause();
    }

    private String tronquer(String texte, int longueur) {
        if (texte.length() <= longueur) return texte;
        return texte.substring(0, longueur - 3) + "...";
    }

    private void modifierComplement() {
        ConsoleHelper.afficherTitre("MODIFIER UN COMPLÉMENT");

        Complement complement = null;

        while (complement == null) {
            int id = ConsoleHelper.lireEntier("ID du complément à modifier");

            try {
                complement = complementService.obtenirComplement(id);
            } catch (EntityNotFoundException e) {
                ConsoleHelper.afficherErreur(e.getMessage());
            }
        }

        try {
            String nom;
            do {
                nom = ConsoleHelper.lireTexte("Nouveau nom du complément (actuel: " + complement.getNom() + ")");

                if (!nom.equals(complement.getNom()) && complementService.existeNom(nom)) {
                    ConsoleHelper.afficherErreur("Ce nom existe déjà. Veuillez en saisir un autre.");
                }

            } while (!nom.equals(complement.getNom()) && complementService.existeNom(nom));

            System.out.println("\nType de complément actuel: " + complement.getType());
            System.out.println("  1. Boisson");
            System.out.println("  2. Frites");

            int typeChoix;
            do {
                typeChoix = ConsoleHelper.lireEntier("Votre choix");
            } while (typeChoix != 1 && typeChoix != 2);

            TypeComplement type = (typeChoix == 1)
                ? TypeComplement.BOISSON
                : TypeComplement.FRITES;

            double prix = ConsoleHelper.lireDecimal("Nouveau prix (FCFA) (actuel: " + complement.getPrix() + ")");
            String image = ConsoleHelper.lireTexte("Nouveau nom de l'image (ex: coca.jpg) (actuel: " + complement.getImage() + ")");

            if (complementService.modifierComplement(complement.getId(), nom, type, prix, image)) {
                ConsoleHelper.afficherSucces("Complément modifié avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }

    private void archiverComplement() {
        ConsoleHelper.afficherTitre("ARCHIVER/RESTAURER UN COMPLÉMENT");

        Complement complement = null;

        while (complement == null) {
            int id = ConsoleHelper.lireEntier("ID du complément à archiver/restaurer");

            try {
                complement = complementService.obtenirComplement(id);
            } catch (EntityNotFoundException e) {
                ConsoleHelper.afficherErreur(e.getMessage());
            }
        }

        try {
            boolean nouvelEtat = !complement.isArchive();
            String action = nouvelEtat ? "archivé" : "restauré";

            if (complementService.modifierComplement(
                complement.getId(),
                complement.getNom(),
                complement.getType(),
                complement.getPrix(),
                complement.getImage()
            )) {
                ConsoleHelper.afficherSucces("Complément " + action + " avec succès !");
            }
        } catch (ValidationException e) {
            ConsoleHelper.afficherErreur("Validation: " + e.getMessage());
        }

        ConsoleHelper.pause();
    }


}
