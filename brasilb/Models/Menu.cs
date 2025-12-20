namespace BrasilBurger.Models
{
    public class Menu
    {
        public int IdMenu { get; set; }
        public string Nom { get; set; }
        public string? Image { get; set; }
        public bool Archive { get; set; }
        public DateTime DateCreation { get; set; }
        public DateTime DateModification { get; set; }
        public List<CompositionMenu>? Compositions { get; set; }
        public decimal PrixTotal { get; set; }
    }
}
