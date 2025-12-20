namespace BrasilBurger.Models
{
    public class CompositionMenu
    {
        public int IdComposition { get; set; }
        public int IdMenu { get; set; }
        public int? IdBurger { get; set; }
        public int? IdComplement { get; set; }
        public int Quantite { get; set; }
        public Burger? Burger { get; set; }
        public Complement? Complement { get; set; }
    }
}