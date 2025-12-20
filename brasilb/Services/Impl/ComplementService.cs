using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class ComplementService : IComplementService
    {
        private readonly IComplementRepository _complementRepository;

        public ComplementService(IComplementRepository complementRepository)
        {
            _complementRepository = complementRepository;
        }

        public List<Complement> GetComplementsDisponibles()
        {
            // Logique métier: retourner tous les compléments non archivés
            return _complementRepository.GetNonArchived();
        }

        public List<Complement> GetBoissons()
        {
            // Logique métier: filtrer par type boisson
            return _complementRepository.GetByType(TypeComplement.boisson);
        }

        public List<Complement> GetFrites()
        {
            // Logique métier: filtrer par type frites
            return _complementRepository.GetByType(TypeComplement.frites);
        }
    }
}