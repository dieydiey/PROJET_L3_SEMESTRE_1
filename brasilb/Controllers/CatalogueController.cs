using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers
{
    public class CatalogueController : Controller
    {
        private readonly IBurgerService _burgerService;
        private readonly IMenuService _menuService;

        public CatalogueController(IBurgerService burgerService, IMenuService menuService)
        {
            _burgerService = burgerService;
            _menuService = menuService;
        }

        public IActionResult Index(string? filtre)
        {
            var viewModel = new CatalogueViewModel
            {
                FiltreType = filtre
            };

            // Logique de filtrage
            if (filtre == "burger")
            {
                viewModel.Burgers = _burgerService.GetCatalogue();
            }
            else if (filtre == "menu")
            {
                viewModel.Menus = _menuService.GetMenusDisponibles();
            }
            else
            {
                // Afficher tout par défaut
                viewModel.Burgers = _burgerService.GetCatalogue();
                viewModel.Menus = _menuService.GetMenusDisponibles();
            }

            return View(viewModel);
        }
    }
}