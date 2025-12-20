using BrasilBurger.Models;

namespace BrasilBurger.Services.Interfaces
{
    public interface IMenuService
    {
        List<Menu> GetMenusDisponibles();
        Menu? GetDetailMenu(int id);
    }
}