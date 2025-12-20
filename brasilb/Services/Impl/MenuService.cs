using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class MenuService : IMenuService
    {
        private readonly IMenuRepository _menuRepository;

        public MenuService(IMenuRepository menuRepository)
        {
            _menuRepository = menuRepository;
        }

        public List<Menu> GetMenusDisponibles()
        {
            // Logique métier: retourner uniquement les menus non archivés avec leur prix
            return _menuRepository.GetNonArchived();
        }

        public Menu? GetDetailMenu(int id)
        {
            var menu = _menuRepository.GetById(id);
            
            // Logique métier: ne pas afficher les menus archivés
            if (menu != null && menu.Archive)
            {
                return null;
            }
            
            return menu;
        }
    }
}