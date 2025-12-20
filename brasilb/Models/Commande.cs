namespace BrasilBurger.Models
{
    public class Commande
    {
        public int IdCommande { get; set; }
        public string NumeroCommande { get; set; }
        public int IdClient { get; set; }
        public DateTime DateCommande { get; set; }
        public ModeConsommation ModeConsommation { get; set; }
        public EtatCommande Etat { get; set; }
        public decimal MontantTotal { get; set; }
        public int? IdZone { get; set; }
        public Utilisateur? Client { get; set; }
        public List<LigneCommande>? LignesCommande { get; set; }
        public Paiement? Paiement { get; set; }
    }
}