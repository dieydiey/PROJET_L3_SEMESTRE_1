using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Controllers
{
    public class CompteController : Controller
    {
        private readonly IAuthService _authService;

        public CompteController(IAuthService authService)
        {
            _authService = authService;
        }

        public IActionResult Connexion(string? returnUrl)
        {
            ViewBag.ReturnUrl = returnUrl;
            return View();
        }

        [HttpPost]
        public IActionResult Connexion(ConnexionViewModel model, string? returnUrl)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var utilisateur = _authService.Connexion(model.Email, model.MotDePasse);

            if (utilisateur == null)
            {
                ModelState.AddModelError("", "Email ou mot de passe incorrect");
                return View(model);
            }

            // Sauvegarder l'utilisateur en session
            HttpContext.Session.SetInt32("IdClient", utilisateur.IdUtilisateur);
            HttpContext.Session.SetString("NomClient", $"{utilisateur.Prenom} {utilisateur.Nom}");
            HttpContext.Session.SetString("EmailClient", utilisateur.Email);

            if (!string.IsNullOrEmpty(returnUrl) && Url.IsLocalUrl(returnUrl))
            {
                return Redirect(returnUrl);
            }

            return RedirectToAction("Index", "Catalogue");
        }

        public IActionResult Inscription()
        {
            return View();
        }

        [HttpPost]
        public IActionResult Inscription(InscriptionViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            if (!_authService.EmailDisponible(model.Email))
            {
                ModelState.AddModelError("Email", "Cet email est déjà utilisé");
                return View(model);
            }

            if (!_authService.TelephoneDisponible(model.Telephone))
            {
                ModelState.AddModelError("Telephone", "Ce numéro de téléphone est déjà utilisé");
                return View(model);
            }

            try
            {
                int idUtilisateur = _authService.Inscription(model);
                
                // Connexion automatique après inscription
                HttpContext.Session.SetInt32("IdClient", idUtilisateur);
                HttpContext.Session.SetString("NomClient", $"{model.Prenom} {model.Nom}");
                HttpContext.Session.SetString("EmailClient", model.Email);

                return RedirectToAction("Index", "Catalogue");
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", "Erreur lors de l'inscription: " + ex.Message);
                return View(model);
            }
        }

        public IActionResult Deconnexion()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Index", "Home");
        }

        public IActionResult Profil()
        {
            var idClient = HttpContext.Session.GetInt32("IdClient");
            
            if (idClient == null)
            {
                return RedirectToAction("Connexion");
            }

            return View();
        }
    }
}