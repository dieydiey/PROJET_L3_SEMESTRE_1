package sn.ism.brasilburger.repositories.impl;

import sn.ism.brasilburger.entity.Burger;
import sn.ism.brasilburger.repositories.interfaces.IBurgerRepository;
import sn.ism.brasilburger.utils.DatabaseConnection;
import sn.ism.brasilburger.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryImpl implements IBurgerRepository {
    private final Connection connection;

    public BurgerRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean save(Burger burger) {
        String sql = "INSERT INTO burger (nom, prix, image, archive) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, burger.getNom());
            stmt.setDouble(2, burger.getPrix());
            stmt.setString(3, burger.getImage());
            stmt.setBoolean(4, burger.isArchive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    burger.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la création du burger: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Burger> findById(Integer id) {
        String sql = "SELECT * FROM burger WHERE id_burger = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération du burger: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Burger> findAll() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burger ORDER BY date_creation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des burgers: " + e.getMessage());
        }
        return burgers;
    }

    @Override
    public Optional<Burger> findByNom(String nom) {
        String sql = "SELECT * FROM burger WHERE LOWER(nom) = LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Burger> findActifs() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE archive = FALSE ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des burgers actifs: " + e.getMessage());
        }
        return burgers;
    }

    @Override
    public boolean update(Burger burger) {
        String sql = "UPDATE burger SET nom = ?, prix = ?, image = ?, archive = ? WHERE id_burger = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, burger.getNom());
            stmt.setDouble(2, burger.getPrix());
            stmt.setString(3, burger.getImage());
            stmt.setBoolean(4, burger.isArchive());
            stmt.setInt(5, burger.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @Override
    public boolean toggleArchive(Integer id) {
        String sql = "UPDATE burger SET archive = NOT archive WHERE id_burger = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM burger WHERE id_burger = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Burger mapResultSetToBurger(ResultSet rs) throws SQLException {
        Burger burger = new Burger();
        burger.setId(rs.getInt("id_burger"));
        burger.setNom(rs.getString("nom"));
        burger.setPrix(rs.getDouble("prix"));
        burger.setImage(rs.getString("image"));
        burger.setArchive(rs.getBoolean("archive"));
        burger.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        burger.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        return burger;
    }
}
