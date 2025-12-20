using BrasilBurger.Models;

namespace BrasilBurger.Services.Interfaces
{
    public interface IComplementService
    {
        List<Complement> GetComplementsDisponibles();
        List<Complement> GetBoissons();
        List<Complement> GetFrites();
    }
}