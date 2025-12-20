using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers
{
    public class CommandeController : Controller
    {
        private readonly ICommandeService _commandeService;
        private readonly IZoneRepository _zoneRepository;

        public CommandeController(ICommandeService commandeService, IZoneRepository zoneRepository)
        {
            _commandeService = commandeService;
            _zoneRepository = zoneRepository;
        }

        public IActionResult Recapitulatif()
        {
            var idClient = HttpContext.Session.GetInt32("IdClient");
            
            if (idClient == null)
            {
                return RedirectToAction("Connexion", "Compte", new { returnUrl = "/Commande/Recapitulatif" });
            }

            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier");
            
            if (panier == null || !panier.Articles.Any())
            {
                return RedirectToAction("Index", "Panier");
            }

            var viewModel = new CommandeViewModel
            {
                Panier = panier,
                Zones = _zoneRepository.GetAll()
            };

            return View(viewModel);
        }

        [HttpPost]
        public IActionResult Confirmer(CommandeViewModel model)
        {
            var idClient = HttpContext.Session.GetInt32("IdClient");
            
            if (idClient == null)
            {
                return RedirectToAction("Connexion", "Compte");
            }

            var panier = HttpContext.Session.GetObjectFromJson<PanierViewModel>("Panier");
            
            if (panier == null || !panier.Articles.Any())
            {
                return RedirectToAction("Index", "Panier");
            }

            model.Panier = panier;

            try
            {
                int idCommande = _commandeService.CreerCommande(model, idClient.Value);
                
                // Vider le panier
                HttpContext.Session.Remove("Panier");
                
                return RedirectToAction("Index", "Paiement", new { idCommande });
            }
            catch (Exception ex)
            {
                return Content("ERREUR : " + ex.Message);
            }
            
        }

        public IActionResult Confirmation(int idCommande)
        {
            var commande = _commandeService.GetCommande(idCommande);
            
            if (commande == null)
            {
                return NotFound();
            }

            return View(commande);
        }

        public IActionResult Detail(int idCommande)
        {
            var commande = _commandeService.GetCommande(idCommande);
            
            if (commande == null)
            {
                return NotFound();
            }

            // Vérifier que c'est bien la commande du client connecté
            var idClient = HttpContext.Session.GetInt32("IdClient");
            if (idClient == null || commande.IdClient != idClient.Value)
            {
                return Forbid();
            }

            return View(commande);
        }


        public IActionResult MesCommandes()
        {
            var idClient = HttpContext.Session.GetInt32("IdClient");
            
            if (idClient == null)
            {
                return RedirectToAction("Connexion", "Compte", new { returnUrl = "/Commande/MesCommandes" });
            }

            var commandes = _commandeService.GetCommandesClient(idClient.Value);
            
            var viewModel = new SuiviCommandeViewModel
            {
                Commandes = commandes
            };

            return View(viewModel);
        }
    }
}