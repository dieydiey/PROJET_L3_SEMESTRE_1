using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers
{
    public class ProduitController : Controller
    {
        private readonly IBurgerService _burgerService;
        private readonly IMenuService _menuService;
        private readonly IComplementService _complementService;

        public ProduitController(
            IBurgerService burgerService, 
            IMenuService menuService, 
            IComplementService complementService)
        {
            _burgerService = burgerService;
            _menuService = menuService;
            _complementService = complementService;
        }

        public IActionResult DetailBurger(int id)
        {
            var burger = _burgerService.GetDetail(id);
            
            if (burger == null)
            {
                return NotFound();
            }

            var viewModel = new DetailProduitViewModel
            {
                Burger = burger,
                ComplementsDisponibles = _complementService.GetComplementsDisponibles()
            };

            return View(viewModel);
        }

        public IActionResult DetailMenu(int id)
        {
            var menu = _menuService.GetDetailMenu(id);
            
            if (menu == null)
            {
                return NotFound();
            }

            var viewModel = new DetailProduitViewModel
            {
                Menu = menu
            };

            return View(viewModel);
        }

        [HttpPost]
        public IActionResult AjouterAuPanier([FromBody] ArticlePanierRequest request)
        {
            // Récupérer ou créer le panier en session
            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier") 
                         ?? new PanierViewModel();

            

            var article = new ArticlePanier
            {
                Type = request.Type,
                Id = request.Id,
                Nom = request.Nom,
                Prix = request.Prix,
                Quantite = request.Quantite,
                Image = request.Image,
                // On mappe les compléments reçus
                Complements = request.Complements ?? new List<ComplementPanier>()
            };

            panier.Articles.Add(article);
            panier.MontantTotal = panier.Articles.Sum(a => a.SousTotal);

            HttpContext.Session.SetObjectAsJson("Panier", panier);

            return Json(new { success = true, message = "Produit ajouté au panier" });
        }
    }

    // Classe pour la requête d'ajout au panier
   public class ArticlePanierRequest
    {
        public string Type { get; set; }
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public int Quantite { get; set; }
        public string? Image { get; set; }
        // Ajoutez cette ligne pour recevoir les compléments
        public List<ComplementPanier>? Complements { get; set; } 
    }
}

// Extensions pour la session (à ajouter à la fin du fichier ou dans un fichier séparé)
public static class SessionExtensions
{
    public static void SetObjectAsJson(this ISession session, string key, object value)
    {
        session.SetString(key, System.Text.Json.JsonSerializer.Serialize(value));
    }

    public static T? GetObjectFromJson<T>(this ISession session, string key)
    {
        var value = session.GetString(key);
        return value == null ? default : System.Text.Json.JsonSerializer.Deserialize<T>(value);
    }
}