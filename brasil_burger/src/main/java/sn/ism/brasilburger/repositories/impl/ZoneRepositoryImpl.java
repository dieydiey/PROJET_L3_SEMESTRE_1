package sn.ism.brasilburger.repositories.impl;

import sn.ism.brasilburger.entity.Zone;
import sn.ism.brasilburger.repositories.interfaces.IZoneRepository;
import sn.ism.brasilburger.utils.DatabaseConnection;
import sn.ism.brasilburger.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZoneRepositoryImpl implements IZoneRepository{
    private final Connection connection;

    public ZoneRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean save(Zone zone) {
        String sql = "INSERT INTO zone (nom, quartiers, prix_livraison) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, zone.getNom());
            stmt.setString(2, zone.getQuartiers());
            stmt.setDouble(3, zone.getPrixLivraison());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    zone.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la création de la zone: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zone ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                zones.add(mapResultSetToZone(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des zones: " + e.getMessage());
        }
        return zones;
    }

    private Zone mapResultSetToZone(ResultSet rs) throws SQLException {
        Zone zone = new Zone();
        zone.setId(rs.getInt("id_zone"));
        zone.setNom(rs.getString("nom"));
        zone.setQuartiers(rs.getString("quartiers"));
        zone.setPrixLivraison(rs.getDouble("prix_livraison"));
        zone.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return zone;
    }
    
    @Override
    public Optional<Zone> findByNom(String nom) {
        String sql = "SELECT * FROM zone WHERE nom = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToZone(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération de la zone: " + e.getMessage());
        }
        return Optional.empty();
    }
}
