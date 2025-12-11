package sn.ism.brasilburger.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    private DatabaseConnection() {
        try {
            // Charger le driver PostgreSQL
            Class.forName("org.postgresql.Driver");

            // 1. Essayer de charger depuis config.properties
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("config.properties");
            
            if (input != null) {
                props.load(input);
                this.url = props.getProperty("db.url");
                this.username = props.getProperty("db.username");
                this.password = props.getProperty("db.password");
                System.out.println("✓ Configuration chargée depuis config.properties");
            } 
            // 2. Sinon, essayer les variables d'environnement
            else if (System.getenv("DB_URL") != null) {
                this.url = System.getenv("DB_URL");
                this.username = System.getenv("DB_USERNAME");
                this.password = System.getenv("DB_PASSWORD");
                System.out.println("✓ Configuration chargée depuis variables d'environnement");
            }
            else {
                System.out.println("⚠ Utilisation de la configuration locale par défaut");
                this.url = "jdbc:postgresql://localhost:5432/brasil_burger";
                this.username = "postgres";
                this.password = "postgres";
            }

        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver PostgreSQL non trouvé!");
            System.err.println("Ajoutez la dépendance PostgreSQL dans pom.xml");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("✗ Erreur de lecture de la configuration!");
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Configuration SSL pour Neon
                Properties props = new Properties();
                props.setProperty("user", username);
                props.setProperty("password", password);
                props.setProperty("ssl", "true");
                props.setProperty("sslmode", "require");
                
                connection = DriverManager.getConnection(url, props);
                System.out.println("✓ Connexion établie à Neon Postgres");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur de connexion à la base de données!");
            System.err.println("URL: " + url);
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la fermeture de la connexion!");
            e.printStackTrace();
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Test de connexion réussi!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Test de connexion échoué!");
            e.printStackTrace();
        }
        return false;
    }
}
