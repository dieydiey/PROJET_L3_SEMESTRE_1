namespace BrasilBurger.Models
{
    public class LigneCommande
    {
        public int IdLigne { get; set; }
        public int IdCommande { get; set; }
        public int? IdBurger { get; set; }
        public int? IdMenu { get; set; }
        public int? IdComplement { get; set; }
        public int Quantite { get; set; }
        public decimal PrixUnitaire { get; set; }
        public decimal SousTotal { get; set; }
        public Burger? Burger { get; set; }
        public Menu? Menu { get; set; }
        public Complement? Complement { get; set; }
    }
}