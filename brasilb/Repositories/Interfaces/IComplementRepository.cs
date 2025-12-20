using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IComplementRepository
    {
        List<Complement> GetAll();
        List<Complement> GetNonArchived();
        Complement? GetById(int id);
        List<Complement> GetByType(TypeComplement type);
        
    }
}