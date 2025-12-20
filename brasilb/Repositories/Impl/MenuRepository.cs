using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class MenuRepository : IMenuRepository
    {
        private readonly DatabaseConfig _dbConfig;

        public MenuRepository(DatabaseConfig dbConfig)
        {
            _dbConfig = dbConfig;
        }

        public List<Menu> GetAll()
        {
            var menus = new List<Menu>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_menu, nom, image, archive, 
                                date_creation, date_modification 
                                FROM menu ORDER BY nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        menus.Add(MapMenu(reader));
                    }
                }
            }
            
            return menus;
        }

        public List<Menu> GetNonArchived()
        {
            var menus = new List<Menu>();

            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();

                string query = @"
                    SELECT 
                        m.id_menu,
                        m.nom,
                        m.image,
                        m.archive,
                        m.date_creation,
                        m.date_modification,
                        COALESCE(SUM(
                            CASE 
                                WHEN cm.id_burger IS NOT NULL THEN b.prix * cm.quantite
                                WHEN cm.id_complement IS NOT NULL THEN c.prix * cm.quantite
                                ELSE 0
                            END
                        ), 0) AS prix_total
                    FROM menu m
                    LEFT JOIN composition_menu cm ON cm.id_menu = m.id_menu
                    LEFT JOIN burger b ON cm.id_burger = b.id_burger
                    LEFT JOIN complement c ON cm.id_complement = c.id_complement
                    WHERE m.archive = false
                    GROUP BY m.id_menu, m.nom, m.image, m.archive, 
                            m.date_creation, m.date_modification
                    ORDER BY m.nom";

                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        var menu = new Menu
                        {
                            IdMenu = reader.GetInt32(0),
                            Nom = reader.GetString(1),
                            Image = reader.IsDBNull(2) ? null : reader.GetString(2),
                            Archive = reader.GetBoolean(3),
                            DateCreation = reader.GetDateTime(4),
                            DateModification = reader.GetDateTime(5),
                            PrixTotal = reader.GetDecimal(6)
                        };

                        menus.Add(menu);
                    }
                }
            }

            return menus;
        }


        public Menu? GetById(int id)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_menu, nom, image, archive, 
                                date_creation, date_modification 
                                FROM menu 
                                WHERE id_menu = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            var menu = MapMenu(reader);
                            menu.PrixTotal = GetPrixTotal(id);
                            menu.Compositions = GetCompositions(id);
                            return menu;
                        }
                    }
                }
            }
            
            return null;
        }

        public List<CompositionMenu> GetCompositions(int idMenu)
        {
            var compositions = new List<CompositionMenu>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT cm.id_composition, cm.id_menu, cm.id_burger, 
                           cm.id_complement, cm.quantite,
                           b.nom as burger_nom, b.prix as burger_prix, 
                           b.image as burger_image,
                           c.nom as complement_nom, c.prix as complement_prix, 
                           c.image as complement_image, c.type as complement_type
                    FROM composition_menu cm
                    LEFT JOIN burger b ON cm.id_burger = b.id_burger
                    LEFT JOIN complement c ON cm.id_complement = c.id_complement
                    WHERE cm.id_menu = @idMenu";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idMenu", idMenu);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var comp = new CompositionMenu
                            {
                                IdComposition = reader.GetInt32(0),
                                IdMenu = reader.GetInt32(1),
                                IdBurger = reader.IsDBNull(2) ? null : reader.GetInt32(2),
                                IdComplement = reader.IsDBNull(3) ? null : reader.GetInt32(3),
                                Quantite = reader.GetInt32(4)
                            };

                            if (!reader.IsDBNull(5))
                            {
                                comp.Burger = new Burger
                                {
                                    IdBurger = reader.GetInt32(2),
                                    Nom = reader.GetString(5),
                                    Prix = reader.GetDecimal(6),
                                    Image = reader.IsDBNull(7) ? null : reader.GetString(7)
                                };
                            }

                            if (!reader.IsDBNull(8))
                            {
                                comp.Complement = new Complement
                                {
                                    IdComplement = reader.GetInt32(3),
                                    Nom = reader.GetString(8),
                                    Prix = reader.GetDecimal(9),
                                    Image = reader.IsDBNull(10) ? null : reader.GetString(10),
                                    Type = Enum.Parse<TypeComplement>(reader.GetString(11), true)
                                };
                            }

                            compositions.Add(comp);
                        }
                    }
                }
            }
            
            return compositions;
        }

        public decimal GetPrixTotal(int idMenu)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT COALESCE(SUM(
                        CASE 
                            WHEN cm.id_burger IS NOT NULL THEN b.prix * cm.quantite
                            WHEN cm.id_complement IS NOT NULL THEN c.prix * cm.quantite
                            ELSE 0
                        END
                    ), 0) as prix_total
                    FROM composition_menu cm
                    LEFT JOIN burger b ON cm.id_burger = b.id_burger
                    LEFT JOIN complement c ON cm.id_complement = c.id_complement
                    WHERE cm.id_menu = @idMenu";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idMenu", idMenu);
                    
                    var result = cmd.ExecuteScalar();
                    return result != DBNull.Value ? Convert.ToDecimal(result) : 0;
                }
            }
        }

        

        private Menu MapMenu(NpgsqlDataReader reader)
        {
            return new Menu
            {
                IdMenu = reader.GetInt32(0),
                Nom = reader.GetString(1),
                Image = reader.IsDBNull(2) ? null : reader.GetString(2),
                Archive = reader.GetBoolean(3),
                DateCreation = reader.GetDateTime(4),
                DateModification = reader.GetDateTime(5)
            };
        }
    }
}