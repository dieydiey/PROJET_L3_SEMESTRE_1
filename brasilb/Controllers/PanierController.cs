using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models.ViewModels;

namespace BrasilBurger.Controllers
{
    public class PanierController : Controller
    {
        public IActionResult Index()
        {
            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier") 
                         ?? new PanierViewModel();

            return View(panier);
        }

        [HttpPost]
        public IActionResult UpdateQuantite(int index, int quantite)
        {
            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier");
            
            if (panier != null && index < panier.Articles.Count && quantite > 0)
            {
                panier.Articles[index].Quantite = quantite;
                panier.MontantTotal = panier.Articles.Sum(a => a.SousTotal);
                
                HttpContext.Session.SetObjectAsJson("Panier", panier);
            }

            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult SupprimerArticle(int index)
        {
            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier");
            
            if (panier != null && index < panier.Articles.Count)
            {
                panier.Articles.RemoveAt(index);
                panier.MontantTotal = panier.Articles.Sum(a => a.SousTotal);
                
                HttpContext.Session.SetObjectAsJson("Panier", panier);
            }

            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult ViderPanier()
        {
            HttpContext.Session.Remove("Panier");
            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult AjouterAuPanier([FromBody] ArticlePanier article)
        {
            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier") 
                        ?? new PanierViewModel();

            panier.Articles.Add(article);
            panier.MontantTotal = panier.Articles.Sum(a => a.SousTotal);

            HttpContext.Session.SetObjectAsJson("Panier", panier);

            return Json(new { success = true });
        }


    }
}