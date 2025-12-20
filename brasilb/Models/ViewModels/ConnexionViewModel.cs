using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Models.ViewModels
{
    public class ConnexionViewModel
    {
        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Email invalide")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Le mot de passe est requis")]
        [DataType(DataType.Password)]
        public string MotDePasse { get; set; }
    }
}