namespace BrasilBurger.Models
{
    public class Utilisateur
    {
        public int IdUtilisateur { get; set; }
        public string Nom { get; set; }
        public string Prenom { get; set; }
        public string Telephone { get; set; }
        public string Email { get; set; }
        public string MotDePasse { get; set; }
        public RoleUtilisateur Role { get; set; }
        public string? Adresse { get; set; }
        public DateTime DateCreation { get; set; }
    }
}