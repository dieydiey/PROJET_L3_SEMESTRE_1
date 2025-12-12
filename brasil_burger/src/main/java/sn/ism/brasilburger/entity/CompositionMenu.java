package sn.ism.brasilburger.entity;

public class CompositionMenu {
    private int id;
    private int idMenu;
    private Integer idBurger;
    private Integer idComplement;
    private int quantite;
    private Burger burger;
    private Complement complement;

    // Constructeurs
    public CompositionMenu() {}

    public CompositionMenu(int idMenu, Integer idBurger, Integer idComplement, int quantite) {
        this.idMenu = idMenu;
        this.idBurger = idBurger;
        this.idComplement = idComplement;
        this.quantite = quantite;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMenu() { return idMenu; }
    public void setIdMenu(int idMenu) { this.idMenu = idMenu; }

    public Integer getIdBurger() { return idBurger; }
    public void setIdBurger(Integer idBurger) { this.idBurger = idBurger; }

    public Integer getIdComplement() { return idComplement; }
    public void setIdComplement(Integer idComplement) { this.idComplement = idComplement; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public Burger getBurger() { return burger; }
    public void setBurger(Burger burger) { this.burger = burger; }

    public Complement getComplement() { return complement; }
    public void setComplement(Complement complement) { this.complement = complement; }

    @Override
    public String toString() {
        if (burger != null) {
            return String.format("  - %dx %s (%.2f FCFA)", 
                    quantite, burger.getNom(), burger.getPrix() * quantite);
        } else if (complement != null) {
            return String.format("  - %dx %s (%.2f FCFA)", 
                    quantite, complement.getNom(), complement.getPrix() * quantite);
        }
        return "Composition invalide";
    }

}
