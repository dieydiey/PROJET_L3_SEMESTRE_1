using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IPaiementRepository
    {
        int Create(Paiement paiement);
        Paiement? GetByCommande(int idCommande);
        void UpdateStatut(int idPaiement, StatutPaiement statut);
    }
}