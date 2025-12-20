using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers
{
    public class PaiementController : Controller
    {
        private readonly IPaiementService _paiementService;
        private readonly ICommandeService _commandeService;

        public PaiementController(IPaiementService paiementService, ICommandeService commandeService)
        {
            _paiementService = paiementService;
            _commandeService = commandeService;
        }

        public IActionResult Index(int idCommande)
        {
            var commande = _commandeService.GetCommande(idCommande);
            
            if (commande == null)
            {
                return NotFound();
            }

            ViewBag.Commande = commande;
            return View();
        }

        [HttpPost]
        public IActionResult Traiter(int idCommande, string methodePaiement)
        {
            var commande = _commandeService.GetCommande(idCommande);
            
            if (commande == null)
            {
                return NotFound();
            }

            var methode = methodePaiement.ToLower() == "wave" 
                ? MethodePaiement.wave 
                : MethodePaiement.om;

            try
            {
                int idPaiement = _paiementService.TraiterPaiement(
                    idCommande, 
                    methode, 
                    commande.MontantTotal
                );

                // Simuler une référence de transaction
                string reference = $"REF-{DateTime.Now:yyyyMMddHHmmss}-{new Random().Next(1000, 9999)}";
                
                _paiementService.ValiderPaiement(idPaiement, reference);

                return RedirectToAction("Confirmation", "Commande", new { idCommande });
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", "Erreur lors du paiement: " + ex.Message);
                ViewBag.Commande = commande;
                return View("Index", idCommande);
            }
        }

        public IActionResult Confirmation(int idCommande)
        {
            var commande = _commandeService.GetCommande(idCommande);
            var paiement = _paiementService.GetPaiementCommande(idCommande);
            
            ViewBag.Commande = commande;
            ViewBag.Paiement = paiement;
            
            return View();
        }
    }
}