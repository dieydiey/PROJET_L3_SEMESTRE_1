using BrasilBurger.Models;

namespace BrasilBurger.Repositories.Interfaces
{
    public interface IZoneRepository
    {
        List<Zone> GetAll();
        Zone? GetById(int id);
    }
}