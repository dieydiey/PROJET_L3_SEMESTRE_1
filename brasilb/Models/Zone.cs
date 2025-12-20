namespace BrasilBurger.Models
{
    public class Zone
    {
        public int IdZone { get; set; }
        public string Nom { get; set; }
        public string Quartiers { get; set; }
        public decimal PrixLivraison { get; set; }
        public DateTime DateCreation { get; set; }
    }
}