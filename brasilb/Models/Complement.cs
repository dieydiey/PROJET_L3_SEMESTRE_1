using BrasilBurger.Models;

namespace BrasilBurger.Models

{
    public class Complement
    {
        public int IdComplement { get; set; }
        public string Nom { get; set; }
        public TypeComplement Type { get; set; }
        public decimal Prix { get; set; }
        public string? Image { get; set; }
        public bool Archive { get; set; }
        public DateTime DateCreation { get; set; }
        public DateTime DateModification { get; set; }
    }
}