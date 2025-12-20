using BrasilBurger.Models;
using BrasilBurger.Models.ViewModels;

namespace BrasilBurger.Services.Interfaces
{
    public interface IAuthService
    {
        Utilisateur? Connexion(string email, string motDePasse);
        int Inscription(InscriptionViewModel model);
        bool EmailDisponible(string email);
        bool TelephoneDisponible(string telephone);
    }
}