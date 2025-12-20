using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface ICommandeRepository
    {
        int Create(Commande commande);
        void AddLigneCommande(LigneCommande ligne);
        Commande? GetById(int id);
        Commande? GetByNumero(string numero);
        List<Commande> GetByClient(int idClient);
        List<Commande> GetByDate(DateTime date);
        List<Commande> GetByEtat(EtatCommande etat);
        void UpdateEtat(int idCommande, EtatCommande etat);
        void Annuler(int idCommande);
    }
}