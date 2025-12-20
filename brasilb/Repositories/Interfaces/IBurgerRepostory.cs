using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IBurgerRepository
    {
        List<Burger> GetAll();
        List<Burger> GetNonArchived();
        Burger? GetById(int id);
       
    }
}