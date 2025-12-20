using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IUtilisateurRepository
    {
        Utilisateur? GetById(int id);
        Utilisateur? GetByEmail(string email);
        Utilisateur? GetByTelephone(string telephone);
        int Create(Utilisateur utilisateur);
        void Update(Utilisateur utilisateur);
        bool EmailExists(string email);
        bool TelephoneExists(string telephone);
    }
}