package sn.ism.brasilburger.utils;

import java.util.Scanner;

public class ConsoleHelper {
     private static final Scanner scanner = new Scanner(System.in);

    public static void afficherTitre(String titre) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   " + titre);
        System.out.println("=".repeat(50));
    }

    public static void afficherSousTitre(String sousTitre) {
        System.out.println("\n--- " + sousTitre + " ---");
    }

    public static void afficherSucces(String message) {
        System.out.println("✓ " + message);
    }

    public static void afficherErreur(String message) {
        System.err.println("✗ ERREUR: " + message);
    }

    public static void afficherInfo(String message) {
        System.out.println("ℹ " + message);
    }

    public static String lireTexte(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public static int lireEntier(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre entier valide");
            }
        }
    }

    public static double lireDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre décimal valide");
            }
        }
    }

    public static boolean confirmer(String message) {
        System.out.print(message + " (o/n): ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        if (reponse.isEmpty()) return false;
        // Accepter plusieurs variantes : 'o', 'oui', 'y', 'yes', '1' ou première lettre
        if (reponse.equals("o") || reponse.equals("oui") || reponse.equals("y") || reponse.equals("yes") || reponse.equals("1")) {
            return true;
        }
        // accepter si la première lettre est 'o' ou 'y' (utile si utilisateur tape 'o ' ou 'oui' partiel)
        char first = reponse.charAt(0);
        return first == 'o' || first == 'y';
    }

    public static void pause() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void afficherLigne() {
        System.out.println("-".repeat(50));
    }
}
