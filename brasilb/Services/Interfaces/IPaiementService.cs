using BrasilBurger.Models;

namespace BrasilBurger.Services.Interfaces
{
    public interface IPaiementService
    {
        int TraiterPaiement(int idCommande, MethodePaiement methode, decimal montant);
        Paiement? GetPaiementCommande(int idCommande);
        bool ValiderPaiement(int idPaiement, string referenceTransaction);
    }
}