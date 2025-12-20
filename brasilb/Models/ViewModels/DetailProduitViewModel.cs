namespace BrasilBurger.Models.ViewModels
{
    public class DetailProduitViewModel
    {
        public Burger? Burger { get; set; }
        public Menu? Menu { get; set; }
        public List<Complement> ComplementsDisponibles { get; set; } = new();
    }
}