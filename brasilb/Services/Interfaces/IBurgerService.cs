using BrasilBurger.Models;

namespace BrasilBurger.Services.Interfaces
{
    public interface IBurgerService
    {
        List<Burger> GetCatalogue();
        Burger? GetDetail(int id);
    }
}