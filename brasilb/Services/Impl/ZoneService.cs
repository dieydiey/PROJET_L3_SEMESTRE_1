using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class ZoneService : IZoneService
    {
        private readonly IZoneRepository _zoneRepository;

        public ZoneService(IZoneRepository zoneRepository)
        {
            _zoneRepository = zoneRepository;
        }

        public List<Zone> GetAll()
        {
            return _zoneRepository.GetAll();
        }

        public Zone? GetById(int id)
        {
            return _zoneRepository.GetById(id);
        }
    }
}
