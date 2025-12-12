package sn.ism.brasilburger.repositories.impl;

import sn.ism.brasilburger.entity.Complement;
import sn.ism.brasilburger.entity.enums.TypeComplement;
import sn.ism.brasilburger.exceptions.DatabaseException;
import sn.ism.brasilburger.repositories.interfaces.IComplementRepository;
import sn.ism.brasilburger.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplementRepositoryImpl implements IComplementRepository {
    private final Connection connection;

    public ComplementRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean save(Complement complement) {
        String sql = "INSERT INTO complement (nom, type, prix, image, archive) VALUES (?, ?::type_complement, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, complement.getNom());
            stmt.setString(2, complement.getType().getValeur());
            stmt.setDouble(3, complement.getPrix());
            stmt.setString(4, complement.getImage());
            stmt.setBoolean(5, complement.isArchive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    complement.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la création du complément: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Complement> findById(Integer id) {
        String sql = "SELECT * FROM complement WHERE id_complement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération du complément: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Complement> findAll() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement ORDER BY date_creation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des compléments: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public boolean update(Complement complement) {
        String sql = "UPDATE complement SET nom = ?, type = ?::type_complement, prix = ?, image = ?, archive = ? WHERE id_complement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, complement.getNom());
            stmt.setString(2, complement.getType().getValeur());
            stmt.setDouble(3, complement.getPrix());
            stmt.setString(4, complement.getImage());
            stmt.setBoolean(5, complement.isArchive());
            stmt.setInt(6, complement.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour du complément: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM complement WHERE id_complement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression du complément: " + e.getMessage());
        }
    }

    @Override
    public Optional<Complement> findByNom(String nom) {
        String sql = "SELECT * FROM complement WHERE nom = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche du complément: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Complement> findByType(String type) {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement WHERE type = ?::type_complement";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche par type: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public List<Complement> findActifs() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement WHERE archive = false ORDER BY date_creation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des compléments actifs: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public boolean toggleArchive(Integer id) {
        String sql = "UPDATE complement SET archive = NOT archive WHERE id_complement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors du changement du statut archive: " + e.getMessage());
        }
    }

    
    private Complement mapResultSetToComplement(ResultSet rs) throws SQLException {
        Complement complement = new Complement();
        complement.setId(rs.getInt("id_complement"));
        complement.setNom(rs.getString("nom"));
        complement.setType(TypeComplement.fromString(rs.getString("type")));
        complement.setPrix(rs.getDouble("prix"));
        complement.setImage(rs.getString("image"));
        complement.setArchive(rs.getBoolean("archive"));
        complement.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        complement.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        return complement;
    }
}