using BrasilBurger.Models;
namespace BrasilBurger.Models
{
    public class Paiement
    {
        public int IdPaiement { get; set; }
        public int IdCommande { get; set; }
        public DateTime DatePaiement { get; set; }
        public decimal Montant { get; set; }
        public MethodePaiement Methode { get; set; }
        public StatutPaiement Statut { get; set; }
        public string? ReferenceTransaction { get; set; }
    }
}