using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Models.ViewModels
{
    public class InscriptionViewModel
    {
        [Required] public string Nom { get; set; }
        [Required] public string Prenom { get; set; }
        [Required] [Phone] public string Telephone { get; set; }
        [Required] [EmailAddress] public string Email { get; set; }
        [Required] [MinLength(6)] [DataType(DataType.Password)] public string MotDePasse { get; set; }
        [Required] [Compare("MotDePasse")] [DataType(DataType.Password)] public string ConfirmationMotDePasse { get; set; }
        public string? Adresse { get; set; }
    }
}