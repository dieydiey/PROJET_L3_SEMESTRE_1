namespace BrasilBurger.Models.ViewModels
{
    public class PanierViewModel
    {
        public List<ArticlePanier> Articles { get; set; } = new();
        public decimal MontantTotal { get; set; }
    }

   public class ArticlePanier
    {
        public string Type { get; set; }
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public int Quantite { get; set; }
        public string? Image { get; set; }

        // ✅ Compléments choisis
        public List<ComplementPanier> Complements { get; set; } = new();

        // ✅ Sous-total incluant compléments
        public decimal SousTotal => (Prix + Complements.Sum(c => c.Prix)) * Quantite;
    }

    public class ComplementPanier
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
    }

}