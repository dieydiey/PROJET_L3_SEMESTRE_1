using BrasilBurger.Models;

namespace BrasilBurger.Services.Interfaces
{
    public interface IZoneService
    {
        List<Zone> GetAll();
        Zone? GetById(int id);
    }
}
