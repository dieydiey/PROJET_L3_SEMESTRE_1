namespace BrasilBurger.Models.ViewModels
{
    public class CatalogueViewModel
    {
        public List<Burger> Burgers { get; set; } = new();
        public List<Menu> Menus { get; set; } = new();
        public string? FiltreType { get; set; }
    }
}