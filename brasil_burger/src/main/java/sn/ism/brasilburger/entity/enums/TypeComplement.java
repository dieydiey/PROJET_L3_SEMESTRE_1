package sn.ism.brasilburger.entity.enums;

public enum TypeComplement {
    BOISSON("boisson"),
    FRITES("frites");

    private final String valeur; 

    TypeComplement(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() { 
        return valeur;
    }

    
    public static TypeComplement fromString(String text) {
        for (TypeComplement type : TypeComplement.values()) {
            if (type.valeur.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de complément invalide: " + text);
    }

    @Override
    public String toString() {
        return valeur;
    }
}