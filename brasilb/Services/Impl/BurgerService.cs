using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class BurgerService : IBurgerService
    {
        private readonly IBurgerRepository _burgerRepository;

        public BurgerService(IBurgerRepository burgerRepository)
        {
            _burgerRepository = burgerRepository;
        }

        public List<Burger> GetCatalogue()
        {
            // Logique métier: retourner uniquement les burgers non archivés
            return _burgerRepository.GetNonArchived();
        }

        public Burger? GetDetail(int id)
        {
            var burger = _burgerRepository.GetById(id);
            
            // Logique métier: ne pas afficher les burgers archivés
            if (burger != null && burger.Archive)
            {
                return null;
            }
            
            return burger;
        }
    }
}