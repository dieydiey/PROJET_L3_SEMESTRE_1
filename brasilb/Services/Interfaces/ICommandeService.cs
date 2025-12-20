using BrasilBurger.Models;
using BrasilBurger.Models.ViewModels;

namespace BrasilBurger.Services.Interfaces
{
    public interface ICommandeService
    {
        int CreerCommande(CommandeViewModel model, int idClient);
        Commande? GetCommande(int id);
        List<Commande> GetCommandesClient(int idClient);
        string GenererNumeroCommande();
    }
}