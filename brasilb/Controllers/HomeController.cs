using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers;

public class HomeController : Controller
{
        private readonly IBurgerService _burgerService;
        private readonly IMenuService _menuService;

        public HomeController(IBurgerService burgerService, IMenuService menuService)
        {
            _burgerService = burgerService;
            _menuService = menuService;
        }

        public IActionResult Index()
        {
            // Récupérer les produits vedettes pour la page d'accueil
            var burgers = _burgerService.GetCatalogue().Take(6).ToList();
            var menus = _menuService.GetMenusDisponibles().Take(6).ToList();
            
            ViewBag.BurgersVedette = burgers;
            ViewBag.MenusVedette = menus;
            
            return View();
        }

        public IActionResult Error()
        {
            return View();
        }
    }
