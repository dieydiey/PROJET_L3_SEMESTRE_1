using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class PaiementService : IPaiementService
    {
        private readonly IPaiementRepository _paiementRepository;
        private readonly ICommandeRepository _commandeRepository;

        public PaiementService(
            IPaiementRepository paiementRepository, 
            ICommandeRepository commandeRepository)
        {
            _paiementRepository = paiementRepository;
            _commandeRepository = commandeRepository;
        }

        public int TraiterPaiement(int idCommande, MethodePaiement methode, decimal montant)
        {
            // Logique métier: créer un paiement en attente
            var paiement = new Paiement
            {
                IdCommande = idCommande,
                Montant = montant,
                Methode = methode,
                Statut = StatutPaiement.en_attente
            };

            int idPaiement = _paiementRepository.Create(paiement);
            
            return idPaiement;
        }

        public Paiement? GetPaiementCommande(int idCommande)
        {
            return _paiementRepository.GetByCommande(idCommande);
        }

        public bool ValiderPaiement(int idPaiement, string referenceTransaction)
        {
            // Logique métier: valider le paiement et mettre à jour le statut de la commande
            _paiementRepository.UpdateStatut(idPaiement, StatutPaiement.valide);
            
            // Optionnel: mettre à jour le statut de la commande
            var paiement = _paiementRepository.GetByCommande(idPaiement);
            if (paiement != null)
            {
                _commandeRepository.UpdateEtat(paiement.IdCommande, EtatCommande.EnCours);
            }
            
            return true;
        }
    }
}