package sn.ism.brasilburger.repositories.impl;

import sn.ism.brasilburger.entity.Livreur;
import sn.ism.brasilburger.repositories.interfaces.ILivreurRepository;
import sn.ism.brasilburger.utils.DatabaseConnection;
import sn.ism.brasilburger.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivreurRepositoryImpl implements ILivreurRepository {
    private final Connection connection;

    public LivreurRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override

    public boolean save(Livreur livreur) {
        String sql = "INSERT INTO livreur (nom, prenom, matricule, telephone, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getMatricule());
            stmt.setString(4, livreur.getTelephone());
            stmt.setBoolean(5, livreur.isDisponible());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    livreur.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la création du livreur: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Livreur> findAll() {
        String sql = "SELECT * FROM livreur";
        List<Livreur> livreurs = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livreurs.add(mapResultSetToLivreur(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des livreurs: " + e.getMessage());
        }
        return livreurs;
    }

    @Override
    public Optional<Livreur> findByTelephone(String telephone) {
        String sql = "SELECT * FROM livreur WHERE telephone = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToLivreur(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche du livreur: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Livreur mapResultSetToLivreur(ResultSet rs) throws SQLException {
        Livreur livreur = new Livreur();
        livreur.setId(rs.getInt("id_livreur"));
        livreur.setNom(rs.getString("nom"));
        livreur.setPrenom(rs.getString("prenom"));
        livreur.setTelephone(rs.getString("telephone"));
        livreur.setMatricule(rs.getString("matricule"));
        livreur.setDisponible(rs.getBoolean("disponible"));
        return livreur;
    }
}
