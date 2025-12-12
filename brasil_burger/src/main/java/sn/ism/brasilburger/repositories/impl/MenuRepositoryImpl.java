package sn.ism.brasilburger.repositories.impl;

import sn.ism.brasilburger.entity.*;
import sn.ism.brasilburger.repositories.interfaces.IMenuRepository;
import sn.ism.brasilburger.utils.DatabaseConnection;
import sn.ism.brasilburger.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements IMenuRepository {
    private final Connection connection;

    public MenuRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean save(Menu menu) {
        String sql = "INSERT INTO menu (nom, image, archive) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, menu.getNom());
            stmt.setString(2, menu.getImage());
            stmt.setBoolean(3, menu.isArchive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    menu.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la création du menu: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean addComposition(CompositionMenu composition) {
        String sql = "INSERT INTO composition_menu (id_menu, id_burger, id_complement, quantite) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, composition.getIdMenu());
            
            if (composition.getIdBurger() != null) {
                stmt.setInt(2, composition.getIdBurger());
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setNull(2, Types.INTEGER);
                stmt.setInt(3, composition.getIdComplement());
            }
            
            stmt.setInt(4, composition.getQuantite());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'ajout de la composition: " + e.getMessage());
        }
    }

    @Override
    public List<CompositionMenu> findCompositionsByMenuId(Integer menuId) {
        List<CompositionMenu> compositions = new ArrayList<>();
        String sql = "SELECT cm.*, b.nom as burger_nom, b.prix as burger_prix, " +
                    "c.nom as complement_nom, c.prix as complement_prix " +
                    "FROM composition_menu cm " +
                    "LEFT JOIN burger b ON cm.id_burger = b.id_burger " +
                    "LEFT JOIN complement c ON cm.id_complement = c.id_complement " +
                    "WHERE cm.id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CompositionMenu comp = new CompositionMenu();
                comp.setId(rs.getInt("id_composition"));
                comp.setIdMenu(rs.getInt("id_menu"));
                comp.setIdBurger((Integer) rs.getObject("id_burger"));
                comp.setIdComplement((Integer) rs.getObject("id_complement"));
                comp.setQuantite(rs.getInt("quantite"));
                
                // Charger burger si existe
                if (comp.getIdBurger() != null) {
                    Burger burger = new Burger();
                    burger.setId(comp.getIdBurger());
                    burger.setNom(rs.getString("burger_nom"));
                    burger.setPrix(rs.getDouble("burger_prix"));
                    comp.setBurger(burger);
                }
                
                // Charger complément si existe
                if (comp.getIdComplement() != null) {
                    Complement complement = new Complement();
                    complement.setId(comp.getIdComplement());
                    complement.setNom(rs.getString("complement_nom"));
                    complement.setPrix(rs.getDouble("complement_prix"));
                    comp.setComplement(complement);
                }
                
                compositions.add(comp);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des compositions: " + e.getMessage());
        }
        return compositions;
    }

    @Override
    public double calculatePrixTotal(Integer menuId) {
        String sql = "SELECT SUM(" +
                    "CASE " +
                    "  WHEN cm.id_burger IS NOT NULL THEN b.prix * cm.quantite " +
                    "  WHEN cm.id_complement IS NOT NULL THEN c.prix * cm.quantite " +
                    "  ELSE 0 " +
                    "END) as prix_total " +
                    "FROM composition_menu cm " +
                    "LEFT JOIN burger b ON cm.id_burger = b.id_burger " +
                    "LEFT JOIN complement c ON cm.id_complement = c.id_complement " +
                    "WHERE cm.id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("prix_total");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors du calcul du prix: " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public Optional<Menu> findById(Integer id) {
        String sql = "SELECT * FROM menu WHERE id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                menu.setCompositions(findCompositionsByMenuId(id));
                menu.setPrixTotal(calculatePrixTotal(id));
                return Optional.of(menu);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération du menu: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY date_creation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                menu.setCompositions(findCompositionsByMenuId(menu.getId()));
                menu.setPrixTotal(calculatePrixTotal(menu.getId()));
                menus.add(menu);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des menus: " + e.getMessage());
        }
        return menus;
    }

    @Override
    public boolean update(Menu menu) {
        String sql = "UPDATE menu SET nom = ?, image = ?, archive = ? WHERE id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, menu.getNom());
            stmt.setString(2, menu.getImage());
            stmt.setBoolean(3, menu.isArchive());
            stmt.setInt(4, menu.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @Override
    public boolean toggleArchive(Integer id) {
        String sql = "UPDATE menu SET archive = NOT archive WHERE id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM menu WHERE id_menu = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("id_menu"));
        menu.setNom(rs.getString("nom"));
        menu.setImage(rs.getString("image"));
        menu.setArchive(rs.getBoolean("archive"));
        menu.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        menu.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        return menu;
    }


}
