using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IMenuRepository
    {
        List<Menu> GetAll();
        List<Menu> GetNonArchived();
        Menu? GetById(int id);
        List<CompositionMenu> GetCompositions(int idMenu);
        decimal GetPrixTotal(int idMenu);
        
    }
}