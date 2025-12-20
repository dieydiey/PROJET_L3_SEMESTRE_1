namespace BrasilBurger.Models.ViewModels
{
    public class CommandeViewModel
    {
        public PanierViewModel Panier { get; set; }
        public ModeConsommation ModeConsommation { get; set; }
        public List<Zone> Zones { get; set; } = new();
        public int? IdZoneSelectionnee { get; set; }
        public string? AdresseLivraison { get; set; }
    }
}