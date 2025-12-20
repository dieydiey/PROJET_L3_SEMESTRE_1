using BrasilBurger.Models;
using BrasilBurger.Models.ViewModels;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Services.Interfaces;

namespace BrasilBurger.Services.Impl
{
    public class AuthService : IAuthService
    {
        private readonly IUtilisateurRepository _utilisateurRepository;

        public AuthService(IUtilisateurRepository utilisateurRepository)
        {
            _utilisateurRepository = utilisateurRepository;
        }

        public Utilisateur? Connexion(string email, string motDePasse)
        {
            // Logique métier: vérifier les credentials
            var utilisateur = _utilisateurRepository.GetByEmail(email);
            
            if (utilisateur != null && VerifierMotDePasse(motDePasse, utilisateur.MotDePasse))
            {
                return utilisateur;
            }
            
            return null;
        }

        public int Inscription(InscriptionViewModel model)
        {
            if (!_utilisateurRepository.EmailExists(model.Email))
            {
                var utilisateur = new Utilisateur
                {
                    Nom = model.Nom,
                    Prenom = model.Prenom,
                    Telephone = model.Telephone,
                    Email = model.Email,
                    MotDePasse = HashMotDePasse(model.MotDePasse),
                    Role = RoleUtilisateur.client,
                    Adresse = model.Adresse
                };

                return _utilisateurRepository.Create(utilisateur);
            }

            throw new Exception("Email déjà utilisé");
        }

        public bool EmailDisponible(string email)
        {
            // Logique métier: vérifier si l'email n'est pas déjà utilisé
            return !_utilisateurRepository.EmailExists(email);
        }

        public bool TelephoneDisponible(string telephone)
        {
            // Logique métier: vérifier si le téléphone n'est pas déjà utilisé
            return !_utilisateurRepository.TelephoneExists(telephone);
        }

        private string HashMotDePasse(string motDePasse)
        {
            // Logique métier: hasher le mot de passe avec BCrypt
            return BCrypt.Net.BCrypt.HashPassword(motDePasse);
        }

        private bool VerifierMotDePasse(string motDePasse, string hash)
        {
            // Logique métier: vérifier le mot de passe avec BCrypt
            return BCrypt.Net.BCrypt.Verify(motDePasse, hash);
        }
    }
}