using BrasilBurger.Models;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class CommandeService : ICommandeService
    {
        private readonly ICommandeRepository _commandeRepository;
        private readonly IZoneRepository _zoneRepository;
        private readonly IPaiementRepository _paiementRepository;

        public CommandeService(
            ICommandeRepository commandeRepository, 
            IZoneRepository zoneRepository,
            IPaiementRepository paiementRepository)
        {
            _commandeRepository = commandeRepository;
            _zoneRepository = zoneRepository;
            _paiementRepository = paiementRepository;
        }

        public int CreerCommande(CommandeViewModel model, int idClient)
        {
            // Logique métier: calculer les frais de livraison si applicable
            decimal fraisLivraison = 0;
            
            if (model.ModeConsommation == ModeConsommation.livraison && model.IdZoneSelectionnee.HasValue)
            {
                var zone = _zoneRepository.GetById(model.IdZoneSelectionnee.Value);
                fraisLivraison = zone?.PrixLivraison ?? 0;
            }

            // Créer la commande
            var commande = new Commande
            {
                NumeroCommande = GenererNumeroCommande(),
                IdClient = idClient,
                ModeConsommation = model.ModeConsommation,
                Etat = EtatCommande.EnAttente,
                MontantTotal = model.Panier.MontantTotal + fraisLivraison,
                IdZone = model.IdZoneSelectionnee
            };

            int idCommande = _commandeRepository.Create(commande);

            // Créer les lignes de commande
            foreach (var article in model.Panier.Articles)
            {
                var ligne = new LigneCommande
                {
                    IdCommande = idCommande,
                    IdBurger = article.Type == "burger" ? article.Id : null,
                    IdMenu = article.Type == "menu" ? article.Id : null,
                    IdComplement = article.Type == "complement" ? article.Id : null,
                    Quantite = article.Quantite,
                    PrixUnitaire = article.Prix,
                    SousTotal = article.SousTotal
                };

                _commandeRepository.AddLigneCommande(ligne);
            }

            return idCommande;
        }

        public Commande? GetCommande(int id)
        {
            var commande = _commandeRepository.GetById(id);

            if (commande != null)
            {
                commande.Paiement = _paiementRepository.GetByCommande(id);
            }

            return commande;
        }


        public List<Commande> GetCommandesClient(int idClient)
    {
        var commandes = _commandeRepository.GetByClient(idClient);

        foreach (var commande in commandes)
        {
            commande.Paiement = _paiementRepository.GetByCommande(commande.IdCommande);
        }

        return commandes;
    }


        public string GenererNumeroCommande()
        {
            // Logique métier: générer un numéro de commande unique
            return $"CMD-{DateTime.Now:yyyyMMddHHmmss}-{new Random().Next(1000, 9999)}";
        }
    }
}